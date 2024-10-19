package com.example.plugins.exception

data class NotFoundException(
    override val message: String
) : RuntimeException(message)
