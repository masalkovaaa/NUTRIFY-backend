package com.example.plugins.extension.db

import org.jetbrains.exposed.sql.transactions.transaction

fun <T> dbQuery(block: () -> T) = transaction { block() }
