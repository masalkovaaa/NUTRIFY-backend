package com.example.app.service.impl

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.PersonalDataDto
import com.example.app.model.Recipe
import com.example.app.repository.IngredientRepository
import com.example.app.repository.MealTimeRepository
import com.example.app.repository.RecipeRepository
import com.example.app.service.FoodService
import com.example.app.service.UploadService
import com.example.app.service.UserService
import com.example.plugins.extension.db.dbQuery
import kotlin.math.abs

class FoodServiceImpl(
    private val mealTimeRepository: MealTimeRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val userService: UserService,
    private val uploadService: UploadService
) : FoodService {

    override fun save(food: FoodCreateDto) = dbQuery {
        val recipeId = recipeRepository.save(food.recipe).id!!
        ingredientRepository.saveAll(food.ingredients, recipeId)
        mealTimeRepository.save(food.mealTypes, recipeId)
    }

    override fun findByMealType(mealType: MealType) =
        recipeRepository.findByMealType(mealType)

    override fun addImageToRecipe(bytes: ByteArray, recipeId: Long) {
        val url = uploadService.uploadFile(bytes)
        recipeRepository.addImage(url, recipeId)
    }

    override fun calculateDiet(userId: Long): List<Recipe> {
        val personalData = userService.findPersonalDataById(userId)
        val typeToCalories = MealType.entries.associateWith { when(it) {
            MealType.BREAKFAST -> personalData.calories.toDouble() / 100.0 * 25
            MealType.LAUNCH -> personalData.calories.toDouble() / 100.0 * 35
            MealType.DINNER -> personalData.calories.toDouble() / 100.0 * 25
            MealType.PART_MEAL -> personalData.calories.toDouble() / 100.0 * 15
        } }
        val result: MutableMap<MealType, List<Recipe>> = mutableMapOf()
        typeToCalories.forEach { (type, calories) ->
            val recipes = when (type) {
                MealType.PART_MEAL -> recipeRepository.findMealByRangeCaloriesAndType((calories * 0.85).toInt(), calories.toInt(), type)
                else -> recipeRepository.findMealByRangeCaloriesAndType((calories * 0.85).toInt(), (calories * 1.15).toInt(), type)
            }
            result[type] = recipes
        }
        return calculate(personalData, result)
    }

    private fun calculate(personalData: PersonalDataDto, map: MutableMap<MealType, List<Recipe>>): ArrayList<Recipe> {
        var closest = 1000.0
        val calories = personalData.calories.toDouble()
        val protein = personalData.weight.toDouble() * 1.8
        val fats = personalData.weight.toDouble()
        val carbs = (calories - protein * 4 - fats * 9) / 4
        val listOfMeals = arrayListOf<Recipe>()
        map[MealType.BREAKFAST]?.forEach { breakfast ->
            map[MealType.LAUNCH]?.forEach { launch ->
                map[MealType.DINNER]?.forEach { dinner ->
                    map[MealType.PART_MEAL]?.forEach { partMeal ->
                        val proteinCalc = (breakfast.protein + launch.protein + dinner.protein + partMeal.protein).toDouble()
                        val fatsCalc = (breakfast.fats + launch.fats + dinner.fats + partMeal.fats).toDouble()
                        val carbsCalc = (breakfast.carbs + launch.carbs + dinner.carbs + partMeal.carbs).toDouble()
                        val caloriesCalc = (breakfast.calories + launch.calories + dinner.calories + partMeal.calories).toDouble()
                        val result = abs((proteinCalc / protein + fatsCalc / fats + carbsCalc / carbs + caloriesCalc / calories) - 4.0)
                        if (result < closest
                            && abs(proteinCalc - protein) / protein < 0.1
                            && abs(fatsCalc - fats) / fats < 0.1
                            && abs(carbsCalc - carbs) / carbs < 0.1
                            && breakfast.id != launch.id
                            && launch.id != dinner.id
                            && breakfast.id != dinner.id
                            )
                        {
                            closest = result
                            listOfMeals.clear()
                            listOfMeals.addAll(listOf(breakfast, launch, dinner, partMeal))
                        }
                    }
                }
            }
        }
        return listOfMeals
    }
}
