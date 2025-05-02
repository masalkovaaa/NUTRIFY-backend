package com.example.app.controller

import com.example.app.dto.auth.LoginRequest
import com.example.app.dto.auth.RegistrationRequest
import com.example.app.service.AuthService
import com.example.plugins.config.Controller
import com.example.plugins.extension.auth.getPrincipal
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AuthController(
    private val authService: AuthService
) : Controller {

    override val setup: Routing.() -> Unit
        get() = {

            route("auth") {

                post("login") {
                    val loginRequest = call.receive<LoginRequest>()
                    val token = authService.login(loginRequest)
                    call.respond(token)
                }

                post("register") {
                    val registrationRequest = call.receive<RegistrationRequest>()
                    val token = authService.register(registrationRequest)
                    call.respond(token)
                }

                authenticate("user") {
                    get("check") {
                        val principal = call.getPrincipal()
                        call.respond(principal.role)
                    }
                }

            }

        }
}
