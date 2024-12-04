package com.example.app.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.app.dto.auth.LoginRequest
import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.auth.Token
import com.example.app.dto.channel.GenerateDietMessage
import com.example.app.model.User
import com.example.app.repository.PersonalDataRepository
import com.example.app.repository.UserRepository
import com.example.app.service.AuthService
import com.example.plugins.config.AppConfig
import com.example.plugins.exception.AuthenticationException
import com.example.plugins.extension.db.dbQuery
import kotlinx.coroutines.channels.Channel
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthServiceImpl(
    private val appConfig: AppConfig,
    private val userRepository: UserRepository,
    private val personalDataRepository: PersonalDataRepository,
    private val channel: Channel<GenerateDietMessage>
) : AuthService {

    override fun login(loginRequest: LoginRequest): Token {
        val authenticationException = AuthenticationException("Wrong email or password")
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw authenticationException
        val isPasswordCorrect = BCrypt.checkpw(loginRequest.password, user.password)
        if (!isPasswordCorrect) {
            throw authenticationException
        }

        return createToken(user)
    }

    override suspend fun register(registrationRequest: RegistrationRequest): Token {
        val isExists = userRepository.existsByEmail(registrationRequest.email)
        if (isExists) {
            throw AuthenticationException("User with such email already exists")
        }

        val user = dbQuery {
            val user = userRepository.save(registrationRequest)
            personalDataRepository.save(registrationRequest, user.id)
            user
        }

        channel.send(GenerateDietMessage(user.id))
        return createToken(user)
    }

    private fun createToken(user: User): Token {
        val value = JWT.create()
            .withAudience(appConfig.security.jwtAudience)
            .withIssuer(appConfig.security.jwtDomain)
            .withClaim("id", user.id)
            .withClaim("email", user.email)
            .withClaim("role", user.role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + 43200000))
            .sign(Algorithm.HMAC256(appConfig.security.jwtSecret))

        return Token(value)
    }
}
