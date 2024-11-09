package com.example.app.repository

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.model.User

interface UserRepository {

    fun findByEmail(email: String): User?

    fun findById(id: Long): User?

    fun existsByEmail(email: String): Boolean

    fun save(registrationRequest: RegistrationRequest): User
}
