package com.example.app.repository

import com.example.app.model.MealType
import com.example.app.model.Recipe

interface RecipeRepository {

    fun save(recipe: Recipe): Recipe

    fun findAll(): List<Recipe>

    fun findById(id: Long): Recipe

    fun findByMealType(mealType: MealType): List<Recipe>

    fun addImage(url: String, recipeId: Long)

    fun findMealByRangeCaloriesAndType(caloriesFrom: Int, caloriesTo: Int, type: MealType): List<Recipe>
}
