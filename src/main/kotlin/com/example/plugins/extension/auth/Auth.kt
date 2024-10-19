package com.example.plugins.extension.auth

import io.ktor.server.auth.jwt.*

fun JWTCredential.validateByRoles(vararg roles: String): JWTPrincipal? {
    val payload = this.payload
    val audience = payload.audience
    val claims = payload.claims

//    if (jwtAudience !in audience) {
//        return null
//    }

    val role = claims["role"]?.asString()
    if (!roles.contains(role)) {
        return null
    }

    return JWTPrincipal(payload)
}
