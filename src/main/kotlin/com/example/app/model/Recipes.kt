package com.example.app.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Recipes : LongIdTable("nutrify.recipes") {
    val name = varchar("name", 255)
    val description = varchar("description", 1024)
    val calories = integer("calories")
    val protein = integer("protein")
    val fats = integer("fats")
    val carbs = integer("carbs")
    val image = varchar("image", 1024).nullable()
}

class RecipeDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeDao>(Recipes)
    var name by Recipes.name
    var description by Recipes.description
    var calories by Recipes.calories
    var protein by Recipes.protein
    var fats by Recipes.fats
    var carbs by Recipes.carbs
    var image by Recipes.image

    val ingredients by IngredientDao referrersOn Ingredients.recipeId

    fun toSerializable() = Recipe(
        id = id.value,
        name = name,
        description = description,
        calories = calories,
        protein = protein,
        fats = fats,
        carbs = carbs,
        image = image,
        ingredients = ingredients.map { it.toSerializable() },
    )
}

data class Recipe(
    val id: Long? = null,
    val name: String,
    val description: String,
    val calories: Int,
    val protein: Int,
    val fats: Int,
    val carbs: Int,
    val image: String?,
    val ingredients: List<Ingredient> = arrayListOf()
)
