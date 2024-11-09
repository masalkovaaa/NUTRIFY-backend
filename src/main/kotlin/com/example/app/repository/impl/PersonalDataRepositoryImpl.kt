package com.example.app.repository.impl

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.calculation.CalculationCaloriesDto
import com.example.app.model.PersonalDataDao
import com.example.app.model.Users
import com.example.app.repository.PersonalDataRepository
import com.example.plugins.extension.calc.calculateCalories
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.dao.id.EntityID

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
}
