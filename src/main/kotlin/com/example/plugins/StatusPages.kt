package com.example.plugins

import com.example.plugins.exception.AuthenticationException
import com.example.plugins.exception.BadRequestException
import com.example.plugins.exception.NotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when(cause) {
                is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause)
                is AuthenticationException -> call.respond(HttpStatusCode.Unauthorized, cause)
                is BadRequestException -> call.respond(HttpStatusCode.BadRequest, cause)
                else -> call.respond(HttpStatusCode.InternalServerError, cause)
            }
        }
    }
}
