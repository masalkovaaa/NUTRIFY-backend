package com.example.app.service

import com.example.app.model.Ingredient
import com.example.app.model.Recipe

interface RecipeService {
    fun findRecipeById(id: Long): Recipe
    fun updateIngredient(ingredient: Ingredient)
    fun updateRecipe(recipe: Recipe)
}
