package com.example.app.dto.user

import com.example.app.model.PersonalDataDto
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val email: String,
    val name: String,
    val personalData: PersonalDataDto,
    val createdAt: LocalDateTime?
)
