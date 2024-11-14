package com.example.app.service.impl

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.Recipe
import com.example.app.repository.IngredientRepository
import com.example.app.repository.MealTimeRepository
import com.example.app.repository.RecipeRepository
import com.example.app.service.FoodService
import com.example.plugins.extension.db.dbQuery

class FoodServiceImpl(
    private val mealTimeRepository: MealTimeRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : FoodService {

    override fun save(food: FoodCreateDto) = dbQuery {
        val recipeId = recipeRepository.save(food.recipe).id!!
        ingredientRepository.saveAll(food.ingredients, recipeId)
        mealTimeRepository.save(food.mealTypes, recipeId)
    }

    override fun findByMealType(mealType: MealType) =
        recipeRepository.findByMealType(mealType)
}
