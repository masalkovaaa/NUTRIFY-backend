package com.example.app.repository

import com.example.app.model.MealType
import com.example.app.model.Recipe

interface RecipeRepository {

    fun save(recipe: Recipe): Recipe

    fun findByMealType(mealType: MealType): List<Recipe>
}
