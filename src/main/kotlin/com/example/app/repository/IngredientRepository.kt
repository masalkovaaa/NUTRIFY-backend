package com.example.app.repository

import com.example.app.model.Ingredient

interface IngredientRepository {

    fun saveAll(ingredients: List<Ingredient>, recipeId: Long)
    fun update(ingredient: Ingredient)
}
