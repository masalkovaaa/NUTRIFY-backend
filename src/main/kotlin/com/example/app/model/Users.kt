package com.example.app.model

import com.example.app.model.enum.Role
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : LongIdTable() {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val username = varchar("username", 1024)
    val password = varchar("password", 1024)
    val role = enumerationByName("role", 10, Role::class)
}

class UserDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDao>(Users)
    var firstName by Users.firstName
    var lastName by Users.lastName
    var username by Users.username
    var password by Users.password
    var role by Users.role

    fun toSerializable() = User(
        id.value,
        username,
        password,
        role
    )
}

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val role: Role
)
