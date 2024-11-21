package com.example.app.repository.impl

import com.example.app.model.*
import com.example.app.repository.RecipeRepository
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.and

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

    override fun addImage(url: String, recipeId: Long): Unit = dbQuery {
        RecipeDao.findByIdAndUpdate(recipeId) {
            it.image = url
        }
    }

    override fun findMealByRangeCaloriesAndType(caloriesFrom: Int, caloriesTo: Int, type: MealType) = dbQuery {
        (MealTime innerJoin Recipes innerJoin Ingredients)
            .select(MealTime.columns + Recipes.columns + Ingredients.columns)
            .where {
                (MealTime.type eq type)
                .and(Recipes.calories greaterEq caloriesFrom)
                .and(Recipes.calories lessEq  caloriesTo)
            }.map { RecipeDao.wrapRow(it) }
            .with(RecipeDao::ingredients)
            .map { it.toSerializable() }
    }
}
