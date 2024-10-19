package com.example.app.model

import com.example.app.dto.User
import com.example.app.model.enum.Role
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : LongIdTable() {
    val username = varchar("username", 1024)
    val password = varchar("password", 1024)
    val role = enumerationByName("role", 10, Role::class)

    fun fromResultRow(row: ResultRow) = User(
        id = row[id].value,
        username = row[username],
        password = row[password],
        role = row[role]
    )
}
