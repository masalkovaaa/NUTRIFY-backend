package com.example.app.repository

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.model.User

interface UserRepository {

    fun findAll(): List<User>

    fun findByUsername(username: String): User?

    fun findById(id: Long): User?

    fun existsByUsername(username: String): Boolean

    fun save(registrationRequest: RegistrationRequest): User
}
