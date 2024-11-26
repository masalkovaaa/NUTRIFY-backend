package com.example.app.service

import com.example.app.dto.user.PersonalDataUpdateDto
import com.example.app.model.PersonalDataDto

interface PersonalDataService {

    fun updatePersonalData(personalDataUpdateDto: PersonalDataUpdateDto, userId: Long): PersonalDataDto

    fun isPossibleToUpdate(userId: Long): Boolean
}
