package com.example.app.repository.impl

import com.example.app.model.*
import com.example.plugins.extension.db.dbQuery
import com.example.plugins.extension.db.toMap
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

interface AdminRepository {
    fun findTableByName(name: String): List<Map<String, Any?>>
}

class AdminRepositoryImpl : AdminRepository {

    companion object {
        private val allTables: List<Table>
            get() = listOf(
                Users, PersonalData, Recipes, Ingredients, MealTime, MealDiet
            )
    }

    override fun findTableByName(name: String) = dbQuery {
        val table = allTables.find { it.tableName.equals("nutrify.$name", ignoreCase = true) }
            ?: error("Table '$name' not found")

        val ans = table.selectAll().map { it.toMap() }
        ans
    }
}