package com.example.app.service.impl

import com.example.app.dto.user.PersonalDataUpdateDto
import com.example.app.repository.PersonalDataRepository
import com.example.app.service.PersonalDataService

class PersonalDataServiceImpl(
    private val personalDataRepository: PersonalDataRepository
) : PersonalDataService {

    override fun updatePersonalData(personalDataUpdateDto: PersonalDataUpdateDto, userId: Long) =
        personalDataRepository.updatePersonalData(personalDataUpdateDto, userId)

    override fun isPossibleToUpdate(userId: Long) = personalDataRepository.isPossibleToUpdate(userId)
}
