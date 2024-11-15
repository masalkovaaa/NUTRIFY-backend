package com.example.app.model

import com.example.app.model.enum.Role
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable("nutrify.users") {
    val name = varchar("name", 255)
    val email = varchar("email", 1024)
    val password = varchar("password", 1024)
    val role = enumerationByName("role", 10, Role::class)
}

class UserDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDao>(Users)
    var name by Users.name
    var email by Users.email
    var password by Users.password
    var role by Users.role

    fun toSerializable() = User(
        id.value,
        email,
        password,
        role
    )
}

data class User(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
)
