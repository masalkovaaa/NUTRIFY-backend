package com.example.app.dto.calculation

import com.example.app.model.Activity
import com.example.app.model.Sex
import com.example.app.model.Target
import java.math.BigDecimal

data class CalculationCaloriesDto(
    val age: Int,
    val height: BigDecimal,
    val weight: BigDecimal,
    val sex: Sex,
    val target: Target,
    val activity: Activity
)
