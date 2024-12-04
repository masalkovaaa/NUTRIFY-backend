package com.example.app.service

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.Recipe
import java.time.LocalDate

interface FoodService {

    fun save(food: FoodCreateDto)

    fun findAll(): List<Recipe>

    fun findByMealType(mealType: MealType): List<Recipe>

    fun addImageToRecipe(bytes: ByteArray, recipeId: Long)

    fun calculateDiet(userId: Long)

    fun findDietByDate(date: LocalDate, userId: Long): List<Recipe>
}
