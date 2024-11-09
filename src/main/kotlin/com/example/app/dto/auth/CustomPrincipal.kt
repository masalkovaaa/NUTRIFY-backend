package com.example.app.dto.auth

data class CustomPrincipal(
    val id: Long,
    val email: String,
    val role: String
)
