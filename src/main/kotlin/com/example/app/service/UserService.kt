package com.example.app.service

import com.example.app.dto.user.UserDto
import com.example.app.dto.user.UserUpdateDto
import com.example.app.model.PersonalDataDto
import com.example.app.model.User

interface UserService {

    fun findPersonalDataById(id: Long): PersonalDataDto

    fun findUserById(id: Long): UserDto

    fun update(userUpdateDto: UserUpdateDto, userId: Long): User
}
