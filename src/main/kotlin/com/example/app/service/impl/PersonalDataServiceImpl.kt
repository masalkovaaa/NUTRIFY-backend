package com.example.app.service.impl

import com.example.app.dto.channel.GenerateDietMessage
import com.example.app.dto.user.PersonalDataUpdateDto
import com.example.app.repository.PersonalDataRepository
import com.example.app.service.PersonalDataService
import kotlinx.coroutines.channels.Channel

class PersonalDataServiceImpl(
    private val personalDataRepository: PersonalDataRepository,
    private val channel: Channel<GenerateDietMessage>
) : PersonalDataService {

    override suspend fun updatePersonalData(personalDataUpdateDto: PersonalDataUpdateDto, userId: Long) {
        personalDataRepository.updatePersonalData(personalDataUpdateDto, userId)
        channel.send(GenerateDietMessage(userId))
    }

    override fun isPossibleToUpdate(userId: Long) = personalDataRepository.isPossibleToUpdate(userId)
}
