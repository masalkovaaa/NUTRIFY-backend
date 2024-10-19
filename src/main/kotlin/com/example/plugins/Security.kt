package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.config.AppConfig
import com.example.plugins.extension.auth.validateByRoles
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(appConfig: AppConfig) {

    val jwtAudience = appConfig.security.jwtAudience
    val jwtDomain = appConfig.security.jwtDomain
    val jwtRealm = appConfig.security.jwtRealm
    val jwtSecret = appConfig.security.jwtSecret

    authentication {
        jwt("user") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                credential.validateByRoles("ADMIN", "USER")
            }
        }

        jwt("admin") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                credential.validateByRoles("ADMIN")
            }
        }
    }
}


