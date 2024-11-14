package com.example.app.dto.food

import com.example.app.model.Ingredient
import com.example.app.model.MealType
import com.example.app.model.Recipe

data class FoodCreateDto(
    val recipe: Recipe,
    val mealTypes: List<MealType>,
    val ingredients: List<Ingredient>
)
