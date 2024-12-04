package com.example.app.dto.food

import com.example.app.model.MealType
import com.example.app.model.Recipe

data class MealDietDto(
    val mealType: MealType,
    val recipe: Recipe
)
