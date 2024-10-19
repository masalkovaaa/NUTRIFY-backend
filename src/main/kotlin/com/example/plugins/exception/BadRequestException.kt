package com.example.plugins.exception

data class BadRequestException(
    override val message: String
) : RuntimeException(message)
