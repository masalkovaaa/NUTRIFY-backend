package com.example.app.service

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.Recipe

interface FoodService {

    fun save(food: FoodCreateDto)

    fun findByMealType(mealType: MealType): List<Recipe>

    fun addImageToRecipe(bytes: ByteArray, recipeId: Long)

    fun calculateDiet(userId: Long): List<Recipe>
}
