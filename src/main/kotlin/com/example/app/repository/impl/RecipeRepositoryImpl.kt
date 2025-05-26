package com.example.app.repository.impl

import com.example.app.model.*
import com.example.app.repository.RecipeRepository
import com.example.plugins.exception.NotFoundException
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    override fun findAll() = dbQuery {
        RecipeDao.all()
            .with(RecipeDao::ingredients)
            .distinctBy { it.name }
            .map { it.toSerializable().addMealTypes() }
    }

    override fun findById(id: Long) = dbQuery {
        RecipeDao.findById(id)?.toSerializable()
            ?: throw NotFoundException("Recipe with ID $id not found")
    }

    override fun findByMealType(mealType: MealType) = dbQuery {
        (Recipes innerJoin MealTime innerJoin Ingredients)
            .select(Recipes.columns + MealTime.columns + Ingredients.columns)
            .where { MealTime.type eq mealType }
            .map { RecipeDao.wrapRow(it) }
            .distinctBy { it.name }
            .with(RecipeDao::ingredients)
            .map { it.toSerializable().addMealTypes() }
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
                .and(Recipes.calories lessEq caloriesTo)
            }.distinctBy { it[Recipes.id] }
            .map { RecipeDao.wrapRow(it) }
            .with(RecipeDao::ingredients)
            .map { it.toSerializable() }
    }

    override fun updateRecipe(recipe: Recipe) = dbQuery {
        RecipeDao.findByIdAndUpdate(recipe.id!!) {
            it.name = recipe.name
            it.description = recipe.description
            it.calories = recipe.calories
            it.protein = recipe.protein
            it.fats = recipe.fats
            it.carbs = recipe.carbs
        }?.toSerializable()
        MealTime.deleteWhere { MealTime.recipeId eq recipe.id }
        recipe.mealTypes.forEach { mealType ->
            MealTimeDao.new {
                this.recipeId = EntityID(recipe.id, Recipes)
                this.type = mealType
            }
        }
    }

    private fun Recipe.addMealTypes(): Recipe = dbQuery {
        this.mealTypes.addAll(
            MealTime.selectAll().where { MealTime.recipeId eq this@addMealTypes.id!! }
                .map { it[MealTime.type] }
        )
        this
    }
}
