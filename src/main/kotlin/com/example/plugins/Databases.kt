package com.example.plugins

import com.example.app.model.Users
import com.example.plugins.config.AppConfig
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(appConfig: AppConfig) {
    val database = Database.connect(
            url = appConfig.database.url,
            user = appConfig.database.user,
            password = appConfig.database.password
    )

    transaction(database) {
        SchemaUtils.createMissingTablesAndColumns(Users)
    }
}
