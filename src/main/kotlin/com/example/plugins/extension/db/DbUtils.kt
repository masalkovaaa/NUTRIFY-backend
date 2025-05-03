package com.example.plugins.extension.db

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction

fun <T> dbQuery(block: () -> T) = transaction { block() }

fun ResultRow.toMap(): Map<String, Any?> =
    this.fieldIndex.keys.associate { column -> column.toString().substringAfterLast('.') to this[column]?.toString() }

