package com.example.app.service

import com.example.app.dto.User

interface UserService {

    fun findAll(): List<User>

    fun findById(id: Long): User
}
