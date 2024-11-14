package com.example.app.repository.impl

import com.example.app.model.MealTimeDao
import com.example.app.model.MealType
import com.example.app.model.Recipes
import com.example.app.repository.MealTimeRepository
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID

class MealTimeRepositoryImpl : MealTimeRepository {

    override fun save(mealTypes: List<MealType>, recipeId: Long) = dbQuery {
        mealTypes.forEach { mealType ->
            MealTimeDao.new {
                this.recipeId = EntityID(recipeId, Recipes)
                this.type = mealType
            }
        }
    }
}
