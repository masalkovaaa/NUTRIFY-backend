package com.example.plugins

import com.example.app.model.*
import com.example.app.model.enum.Role
import com.example.plugins.config.AppConfig
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

fun Application.configureDatabases(appConfig: AppConfig) {
    val database = Database.connect(
            url = appConfig.database.url,
            user = appConfig.database.user,
            password = appConfig.database.password
    )

    transaction(database) {
        exec("SET search_path TO nutrify")
        SchemaUtils.createMissingTablesAndColumns(Users, PersonalData, Recipes, Ingredients, MealTime)
        Users.upsert {
            it[id] = 1
            it[name] = "admin"
            it[email] = "admin"
            it[password] = BCrypt.hashpw("admin", BCrypt.gensalt())
            it[role] = Role.ADMIN
        }
    }
}
