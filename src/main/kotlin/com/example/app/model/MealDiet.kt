package com.example.app.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object MealDiet : LongIdTable("nutrify.meal_diet") {
    val userId = reference("user_id", Users)
    val mealType = enumerationByName<MealType>("type", 20)
    val recipeId = reference("recipe_id", Recipes)
    val date = date("date")
}

class MealDietDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MealDietDao>(MealDiet)
    var userId by MealDiet.userId
    var mealType by MealDiet.mealType
    var recipeId by MealDiet.recipeId
    var recipe by RecipeDao referencedOn MealDiet.recipeId
    var date by MealDiet.date

    fun toSerializable() = MealDietEntity(
        userId = userId.value,
        mealType = mealType,
        recipe = recipe.toSerializable(),
        date = date
    )
}

data class MealDietEntity(
    val userId: Long,
    val mealType: MealType,
    val recipe: Recipe,
    val date: LocalDate
)
