package com.example.app.dto

import com.example.app.model.enum.Role

data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val role: Role
)
