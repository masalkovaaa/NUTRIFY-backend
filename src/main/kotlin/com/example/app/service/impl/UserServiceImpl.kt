package com.example.app.service.impl

import com.example.app.repository.PersonalDataRepository
import com.example.app.repository.UserRepository
import com.example.app.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val personalDataRepository: PersonalDataRepository
) : UserService {

    override fun findPersonalDataById(id: Long) = personalDataRepository.findByUserId(id)
}
