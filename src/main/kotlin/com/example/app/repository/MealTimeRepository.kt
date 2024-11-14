package com.example.app.repository

import com.example.app.model.MealType

interface MealTimeRepository {

    fun save(mealTypes: List<MealType>, recipeId: Long)
}
