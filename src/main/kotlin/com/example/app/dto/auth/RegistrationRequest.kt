package com.example.app.dto.auth

data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
)
