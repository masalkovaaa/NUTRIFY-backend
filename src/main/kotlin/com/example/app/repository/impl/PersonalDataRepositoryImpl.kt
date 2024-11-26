package com.example.app.repository.impl

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.calculation.CalculationCaloriesDto
import com.example.app.dto.user.PersonalDataUpdateDto
import com.example.app.model.PersonalData
import com.example.app.model.PersonalDataDao
import com.example.app.model.Users
import com.example.app.repository.PersonalDataRepository
import com.example.plugins.exception.BadRequestException
import com.example.plugins.exception.NotFoundException
import com.example.plugins.extension.calc.calculateCalories
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class PersonalDataRepositoryImpl : PersonalDataRepository {

    override fun save(registrationRequest: RegistrationRequest, userId: Long) = dbQuery {
        val (_, age, height, weight, sex, target, activity, _, _) = registrationRequest
        PersonalDataDao.new {
            this.userId = EntityID(userId, Users)
            this.age = age
            this.height = height
            this.weight = weight
            this.sex = sex
            this.target = target
            this.activity = activity
            this.calories = calculateCalories(CalculationCaloriesDto(age, height, weight, sex, target, activity))
        }.toSerializable()
    }

    override fun findByUserId(id: Long) = dbQuery {
        PersonalDataDao.find { PersonalData.userId eq id }
            .firstOrNull()
            ?.toSerializable()
            ?: throw NotFoundException("PersonalData not found")
    }

    override fun updatePersonalData(personalDataUpdateDto: PersonalDataUpdateDto, userId: Long) = dbQuery {
        val personalData = PersonalDataDao.find { PersonalData.userId eq userId }
            .firstOrNull() ?: throw NotFoundException("PersonalData not found")

        if (LocalDateTime.now().isBefore(personalData.updatedAt.plusDays(14))) {
            throw BadRequestException("You can't update personal data till ${personalData.updatedAt.plusDays(14)}")
        }

        personalData.apply {
            this.age = personalDataUpdateDto.age
            this.height = personalDataUpdateDto.height
            this.weight = personalDataUpdateDto.weight
            this.target = personalDataUpdateDto.target
            this.activity = personalDataUpdateDto.activity
            this.calories = calculateCalories(CalculationCaloriesDto(personalDataUpdateDto.age, personalDataUpdateDto.height, personalDataUpdateDto.weight, sex, personalDataUpdateDto.target, personalDataUpdateDto.activity))
            this.updatedAt = LocalDateTime.now()
        }.toSerializable()
    }

    override fun isPossibleToUpdate(userId: Long) = dbQuery {
        val personalData = PersonalDataDao.find { PersonalData.userId eq userId }
            .firstOrNull() ?: throw NotFoundException("PersonalData not found")
        LocalDateTime.now().isAfter(personalData.updatedAt.plusDays(14))
    }
}
