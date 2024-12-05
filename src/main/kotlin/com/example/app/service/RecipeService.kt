package com.example.app.service

import com.example.app.model.Recipe

interface RecipeService {

    fun findRecipeById(id: Long): Recipe
}
