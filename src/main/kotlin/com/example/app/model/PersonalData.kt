package com.example.app.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import java.math.BigDecimal

object PersonalData : LongIdTable("personal_data") {
    val userId = reference("user_id", Users)
    val age = integer("age")
    val height = decimal("height", 10, 1)
    val weight = decimal("weight", 10, 2)
    val sex = enumerationByName<Sex>("sex", 10)
    val target = enumerationByName<Target>("target", 20)
    val activity = enumerationByName<Activity>("activity", 20)
    var calories = integer("calories")
}

class PersonalDataDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PersonalDataDao>(PersonalData)
    var age by PersonalData.age
    var height by PersonalData.height
    var weight by PersonalData.weight
    var sex by PersonalData.sex
    var target by PersonalData.target
    var activity by PersonalData.activity
    var userId by PersonalData.userId
    var calories by PersonalData.calories

    fun toSerializable() = PersonalDataDto(
        id.value,
        age,
        height,
        weight,
        sex,
        target,
        activity,
        calories
    )
}

data class PersonalDataDto(
    val id: Long,
    val age: Int,
    val height: BigDecimal,
    val weight: BigDecimal,
    val sex: Sex,
    val target: Target,
    val activity: Activity,
    val calories: Int
)

enum class Sex {
    MALE, FEMALE
}

enum class Target {
    LOSS, RETAIN, GAIN
}

enum class Activity(val value: Double) {
    MINIMAL(1.2),
    WEAK(1.375),
    MIDDLE(1.55),
    HARD(1.7),
    EXTREME(1.9)
}
