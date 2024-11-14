package com.example.app.repository.impl

import com.example.app.model.*
import com.example.app.repository.RecipeRepository
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with

class RecipeRepositoryImpl : RecipeRepository {

    override fun save(recipe: Recipe) = dbQuery {
        RecipeDao.new {
            this.name = recipe.name
            this.description = recipe.description
            this.calories = recipe.calories
            this.protein = recipe.protein
            this.fats = recipe.fats
            this.carbs = recipe.carbs
        }.toSerializable()
    }

    override fun findByMealType(mealType: MealType) = dbQuery {
        MealTimeDao.find { MealTime.type eq mealType }
            .with(MealTimeDao::recipe)
            .with(RecipeDao::ingredients)
            .map { it.recipe.toSerializable() }
    }
}
