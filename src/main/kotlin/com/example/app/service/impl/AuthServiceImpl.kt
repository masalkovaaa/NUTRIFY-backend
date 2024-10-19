package com.example.app.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.app.dto.User
import com.example.app.dto.auth.LoginRequest
import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.auth.Token
import com.example.app.repository.UserRepository
import com.example.app.service.AuthService
import com.example.plugins.config.AppConfig
import com.example.plugins.exception.AuthenticationException
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthServiceImpl(
    private val appConfig: AppConfig,
    private val userRepository: UserRepository
) : AuthService {

    override fun login(loginRequest: LoginRequest): Token {
        val authenticationException = AuthenticationException("Wrong username or password")
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw authenticationException
        val isPasswordCorrect = BCrypt.checkpw(loginRequest.password, user.password)
        if (!isPasswordCorrect) {
            throw authenticationException
        }

        return createToken(user)
    }

    override fun register(registrationRequest: RegistrationRequest): Token {
        val isExists = userRepository.existsByUsername(registrationRequest.username)
        if (isExists) {
            throw AuthenticationException("User with such username already exists")
        }

        val user = userRepository.save(registrationRequest)

        return createToken(user)
    }

    private fun createToken(user: User): Token {
        val value = JWT.create()
            .withAudience(appConfig.security.jwtAudience)
            .withIssuer(appConfig.security.jwtRealm)
            .withClaim("username", user.username)
            .withClaim("role", user.role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(appConfig.security.jwtSecret))

        return Token(value)
    }
}
