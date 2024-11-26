package com.example.app.repository

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.user.UserUpdateDto
import com.example.app.model.User

interface UserRepository {

    fun findByEmail(email: String): User?

    fun findById(id: Long): User?

    fun existsByEmail(email: String): Boolean

    fun save(registrationRequest: RegistrationRequest): User

    fun update(userUpdateDto: UserUpdateDto, userId: Long): User
}
