package com.masalkova.app.service.impl

import com.example.app.dto.User
import com.example.plugins.exception.NotFoundException
import com.example.app.repository.UserRepository
import com.example.app.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun findAll(): List<User> {
        return userRepository.findAll()
    }

    override fun findById(id: Long): User {
        return userRepository.findById(id)
            ?: throw NotFoundException("User with id $id not found")
    }
}
