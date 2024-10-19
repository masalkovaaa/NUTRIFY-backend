package com.example.app.service

import com.example.app.dto.auth.LoginRequest
import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.auth.Token

interface AuthService {

    fun login(loginRequest: LoginRequest): Token

    fun register(registrationRequest: RegistrationRequest): Token
}
