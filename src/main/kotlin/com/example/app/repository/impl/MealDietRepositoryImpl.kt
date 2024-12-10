package com.example.app.repository.impl

import com.example.app.dto.food.MealDietDto
import com.example.app.model.*
import com.example.app.repository.MealDietRepository
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import java.time.LocalDate

class MealDietRepositoryImpl : MealDietRepository {

    override fun saveAll(diet: List<List<MealDietDto>>, userId: Long): Unit = dbQuery {
        var currentDate = LocalDate.now().minusDays(1)
        val data = mutableListOf<Pair<LocalDate, MealDietDto>>()
        diet.forEach { dayDiet ->
            repeat(2) {
                currentDate = currentDate.plusDays(1)
                dayDiet.forEach { mealDietDto ->
                    data.add(currentDate to mealDietDto)
                }
            }
        }

        MealDiet.batchInsert(data) { row ->
            this[MealDiet.userId] = EntityID(userId, Users)
            this[MealDiet.mealType] = row.second.mealType
            this[MealDiet.recipeId] = EntityID(row.second.recipe.id!!, Recipes)
            this[MealDiet.date] = row.first
        }
    }

    override fun findDietByDate(date: LocalDate, userId: Long) = dbQuery {
        (MealDiet innerJoin Recipes)
            .select(MealDiet.columns + Recipes.columns)
            .where { (MealDiet.userId eq userId).and(MealDiet.date eq date) }
            .map { MealDietDto(it[MealDiet.mealType], RecipeDao.wrapRow(it).toSerializable()) }
            .sortedBy { it.mealType.value }
    }

}
