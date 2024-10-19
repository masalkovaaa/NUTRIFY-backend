package com.example.plugins.extension.db

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction

fun <T> dbQuery(block: () -> T) = transaction { block() }

fun <R> ResultRow.map(block: (ResultRow) -> R): R = block(this)
