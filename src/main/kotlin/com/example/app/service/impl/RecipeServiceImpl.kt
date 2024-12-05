package com.example.app.service.impl

import com.example.app.repository.RecipeRepository
import com.example.app.service.RecipeService

class RecipeServiceImpl(
    private val recipeRepository: RecipeRepository
) : RecipeService {

    override fun findRecipeById(id: Long) = recipeRepository.findById(id)
}
