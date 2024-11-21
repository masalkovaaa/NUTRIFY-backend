package com.example.plugins.config

data class AppConfig(
    val database: DatabaseConfig,
    val security: SecurityConfig,
    val s3: S3Properties
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

data class S3Properties(
    val name: String,
    val accessKeyId: String,
    val secretAccessKey: String,
    val serviceEndpoint: String,
    val region: String
)
