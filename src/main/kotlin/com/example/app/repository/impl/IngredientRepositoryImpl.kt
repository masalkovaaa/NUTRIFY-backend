package com.example.app.repository.impl

import com.example.app.model.Ingredient
import com.example.app.model.IngredientDao
import com.example.app.model.Recipes
import com.example.app.repository.IngredientRepository
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID

class IngredientRepositoryImpl : IngredientRepository {

    override fun saveAll(ingredients: List<Ingredient>, recipeId: Long) = dbQuery {
        ingredients.forEach { ingredient ->
            IngredientDao.new {
                this.name = ingredient.name
                this.weight = ingredient.weight
                this.weightType = ingredient.weightType
                this.recipeId = EntityID(recipeId, Recipes)
            }
        }
    }
}
