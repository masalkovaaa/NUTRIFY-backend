package com.example.plugins.exception

data class AuthenticationException(
    override val message: String
) : RuntimeException(message)


