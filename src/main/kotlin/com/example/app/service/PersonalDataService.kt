package com.example.app.service

import com.example.app.dto.user.PersonalDataUpdateDto

interface PersonalDataService {

    suspend fun updatePersonalData(personalDataUpdateDto: PersonalDataUpdateDto, userId: Long)

    fun isPossibleToUpdate(userId: Long): Boolean
}
