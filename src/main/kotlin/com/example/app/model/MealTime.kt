package com.example.app.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object MealTime : LongIdTable("nutrify.meail_time") {
    val recipeId = reference("recipe_id", Recipes)
    val type = enumerationByName<MealType>("type", 15)
}

class MealTimeDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MealTimeDao>(MealTime)
    var recipeId by MealTime.recipeId
    var type by MealTime.type

    val recipe by RecipeDao referencedOn MealTime.recipeId
}

enum class MealType(val percentage: Double) {
    BREAKFAST(0.25), LAUNCH(0.35), DINNER(0.25), PART_MEAL(0.15);

    fun calculateCalories(calories: Double) = calories * percentage
}
