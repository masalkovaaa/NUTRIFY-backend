package com.example.app.service

import com.example.app.model.PersonalDataDto

interface UserService {

    fun findPersonalDataById(id: Long): PersonalDataDto
}
