package com.example.app.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Ingredients : LongIdTable("nutrify.ingredients") {
    val name = varchar("name", 255)
    val weight = integer("weight")
    val weightType = enumerationByName<WeightType>("weight_type", 10)
    val recipeId = reference("recipe_id", Recipes)
}

class IngredientDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<IngredientDao>(Ingredients)
    var name by Ingredients.name
    var weight by Ingredients.weight
    var weightType by Ingredients.weightType
    var recipeId by Ingredients.recipeId

    fun toSerializable() = Ingredient(
        id = id.value,
        name = name,
        weight = weight,
        weightType = weightType
    )
}

data class Ingredient(
    val id: Long? = null,
    val name: String,
    val weight: Int,
    val weightType: WeightType,
)

enum class WeightType {
    GRAM, COUNT
}
