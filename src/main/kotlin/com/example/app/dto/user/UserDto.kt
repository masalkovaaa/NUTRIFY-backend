package com.example.app.dto.user

import com.example.app.model.PersonalDataDto

data class UserDto(
    val id: Long,
    val email: String,
    val personalData: PersonalDataDto
)
