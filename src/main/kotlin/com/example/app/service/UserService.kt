package com.example.app.service

import com.example.app.model.User

interface UserService {

    fun findAll(): List<User>

    fun findById(id: Long): User
}
