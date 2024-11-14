package com.example.app.service

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.Recipe

interface FoodService {

    fun save(food: FoodCreateDto)

    fun findByMealType(mealType: MealType): List<Recipe>
}
