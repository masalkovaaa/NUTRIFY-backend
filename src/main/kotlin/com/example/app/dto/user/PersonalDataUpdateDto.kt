package com.example.app.dto.user

import com.example.app.model.Activity
import com.example.app.model.Target
import java.math.BigDecimal

data class PersonalDataUpdateDto(
    val age: Int,
    val height: BigDecimal,
    val weight: BigDecimal,
    val target: Target,
    val activity: Activity
)
