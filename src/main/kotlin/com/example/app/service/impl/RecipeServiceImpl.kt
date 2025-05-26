package com.example.app.service.impl

import com.example.app.model.Ingredient
import com.example.app.model.Recipe
import com.example.app.repository.IngredientRepository
import com.example.app.repository.RecipeRepository
import com.example.app.service.RecipeService

class RecipeServiceImpl(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : RecipeService {

    override fun findRecipeById(id: Long) = recipeRepository.findById(id)
    override fun updateIngredient(ingredient: Ingredient) = ingredientRepository.update(ingredient)
    override fun updateRecipe(recipe: Recipe) {
        recipeRepository.updateRecipe(recipe)
    }
}
