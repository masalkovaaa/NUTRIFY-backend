package com.example.app.service.impl

import com.example.app.model.User
import com.example.app.repository.UserRepository
import com.example.app.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

}
