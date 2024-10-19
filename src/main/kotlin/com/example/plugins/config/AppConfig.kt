package com.example.plugins.config

data class AppConfig(
    val database: DatabaseConfig,
    val security: SecurityConfig
)

data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String
)

data class SecurityConfig(
    val jwtAudience: String,
    val jwtDomain: String,
    val jwtRealm: String,
    val jwtSecret: String
)
