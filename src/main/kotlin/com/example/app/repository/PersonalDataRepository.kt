package com.example.app.repository

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.model.PersonalDataDto

interface PersonalDataRepository {

    fun save(registrationRequest: RegistrationRequest, userId: Long): PersonalDataDto
}
