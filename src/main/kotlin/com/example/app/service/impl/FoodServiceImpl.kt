package com.example.app.service.impl

import com.example.app.dto.channel.GenerateDietMessage
import com.example.app.dto.food.FoodCreateDto
import com.example.app.dto.food.FoodCharacteristicDto
import com.example.app.dto.food.MealDietDto
import com.example.app.model.MealType
import com.example.app.model.PersonalDataDto
import com.example.app.model.Recipe
import com.example.app.repository.IngredientRepository
import com.example.app.repository.MealDietRepository
import com.example.app.repository.MealTimeRepository
import com.example.app.repository.RecipeRepository
import com.example.app.service.FoodService
import com.example.app.service.UploadService
import com.example.app.service.UserService
import com.example.plugins.extension.db.dbQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs

class FoodServiceImpl(
    private val mealTimeRepository: MealTimeRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val userService: UserService,
    private val uploadService: UploadService,
    private val mealDietRepository: MealDietRepository,
    private val channel: Channel<GenerateDietMessage>
) : FoodService {

    companion object {
        private const val ERROR_PERCENTAGE = 0.05
        private const val MEAL_DAY_COUNT = 7
        private val workers = 3
        private val executor = Executors.newFixedThreadPool(workers)
        private val scope = CoroutineScope(executor.asCoroutineDispatcher())
    }

    private val consumers = (1..workers).map {
        scope.launch { consume() }
    }

    override fun save(food: FoodCreateDto) = dbQuery {
        val recipeId = recipeRepository.save(food.recipe).id!!
        ingredientRepository.saveAll(food.ingredients, recipeId)
        mealTimeRepository.save(food.mealTypes, recipeId)
    }

    override fun findAll() = recipeRepository.findAll()

    override fun findByMealType(mealType: MealType) =
        recipeRepository.findByMealType(mealType)

    override fun addImageToRecipe(bytes: ByteArray, recipeId: Long) {
        val url = uploadService.uploadFile(bytes)
        recipeRepository.addImage(url, recipeId)
    }

    override fun calculateDiet(userId: Long) {
        val personalData = userService.findPersonalDataById(userId)
        val typeToCalories = MealType.entries.associateWith { it.calculateCalories(personalData.calories.toDouble()) }
        val result: MutableMap<MealType, List<Recipe>> = mutableMapOf()
        typeToCalories.forEach { (type, calories) ->
            val recipes = when (type) {
                MealType.PART_MEAL -> recipeRepository.findMealByRangeCaloriesAndType((calories * 0.85).toInt(), calories.toInt(), type)
                else -> recipeRepository.findMealByRangeCaloriesAndType((calories * 0.85).toInt(), (calories * 1.15).toInt(), type)
            }
            result[type] = recipes
        }
        val calculationResult= calculate(personalData, result)
        mealDietRepository.saveAll(calculationResult, userId)
    }

    override fun findDietByDate(date: LocalDate, userId: Long) = mealDietRepository.findDietByDate(date, userId)

    private fun calculate(personalData: PersonalDataDto, map: MutableMap<MealType, List<Recipe>>, mealDayCount: Int = MEAL_DAY_COUNT): List<List<MealDietDto>> {
        val queue = PriorityQueue<Pair<Double, List<MealDietDto>>>(compareByDescending { it.first })
        val targetCharacteristic = calculateTargetFoodCharacteristic(personalData)
        map[MealType.BREAKFAST]?.forEach { breakfast ->
            map[MealType.LAUNCH]?.forEach { launch ->
                map[MealType.DINNER]?.forEach { dinner ->
                    map[MealType.PART_MEAL]?.forEach { partMeal ->
                        val mealCharacteristic = mapToFoodCharacteristic(listOf(breakfast, launch, dinner, partMeal))
                        val result = mealCharacteristic.compare(targetCharacteristic)
                        if (mealCharacteristic.isFitInTarget(targetCharacteristic)
                            && setOf(breakfast.id, launch.id, dinner.id, partMeal.id).size == 4
                            && (queue.size < mealDayCount || queue.peek().first > result)
                            )
                        {
                            queue.add(Pair(
                                result,
                                listOf(MealDietDto(MealType.BREAKFAST, breakfast),
                                    MealDietDto(MealType.LAUNCH, launch),
                                    MealDietDto(MealType.DINNER, dinner),
                                    MealDietDto(MealType.PART_MEAL, partMeal)
                                )
                            ))
                            if (queue.size > mealDayCount) queue.poll()
                        }
                    }
                }
            }
        }
        return queue.map { it.second }.toList()
    }

    private fun calculateTargetFoodCharacteristic(personalData: PersonalDataDto): FoodCharacteristicDto {
        val calories = personalData.calories.toDouble()
        val protein = personalData.weight.toDouble() * 1.8
        val fats = personalData.weight.toDouble()
        return FoodCharacteristicDto(
            calories,
            protein,
            fats,
            (calories - protein * 4 - fats * 9) / 4
        )
    }

    private fun mapToFoodCharacteristic(recipes: List<Recipe>) = FoodCharacteristicDto(
        recipes.sumOf { it.calories }.toDouble(),
        recipes.sumOf { it.protein }.toDouble(),
        recipes.sumOf { it.fats }.toDouble(),
        recipes.sumOf { it.carbs }.toDouble()
    )

    private fun FoodCharacteristicDto.compare(another: FoodCharacteristicDto) =
        abs((this.protein / another.protein + this.fats / another.fats + this.carbs / carbs + this.calories / another.calories) - 4.0)

    private fun FoodCharacteristicDto.isFitInTarget(target: FoodCharacteristicDto) =
        abs(this.protein - target.protein) / target.protein < ERROR_PERCENTAGE
                && abs(this.fats - target.fats) / target.fats < ERROR_PERCENTAGE
                && abs(this.carbs - target.carbs) / target.carbs < ERROR_PERCENTAGE

    private suspend fun consume() {
        for (msg in channel) {
            calculateDiet(msg.userId)
        }
    }
}
