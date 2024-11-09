package com.example.plugins.extension.calc

import com.example.app.dto.calculation.CalculationCaloriesDto
import com.example.app.model.Sex
import com.example.app.model.Target

fun calculateCalories(dto: CalculationCaloriesDto): Int {
    val coef = when(dto.sex) {
        Sex.MALE -> 5
        Sex.FEMALE -> -161
    }
    val targetCoef = when(dto.target) {
        Target.LOSS -> 0.8
        Target.RETAIN -> 1.0
        Target.GAIN -> 1.2
    }
    return ((10 * dto.weight.toDouble() + 6.25 * dto.height.toDouble() - 5 * dto.age + coef) * dto.activity.value * targetCoef).toInt()
}
