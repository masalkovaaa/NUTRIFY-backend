package com.example.plugins.extension.auth

import io.ktor.server.auth.jwt.*

fun JWTCredential.validateByRoles(roles: Set<String>): JWTPrincipal? {
    val payload = this.payload
    val audience = payload.audience
    val claims = payload.claims

    val role = claims["role"]?.asString()
    if (!roles.contains(role)) {
        return null
    }

    return JWTPrincipal(payload)
}
