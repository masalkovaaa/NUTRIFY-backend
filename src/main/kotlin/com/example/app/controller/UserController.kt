package com.example.app.controller

import com.example.app.dto.user.UserUpdateDto
import com.example.app.service.UserService
import com.example.plugins.config.Controller
import com.example.plugins.extension.auth.getPrincipal
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserController(
    private val userService: UserService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("users") {
                authenticate("user") {
                    put {
                        val principal = call.getPrincipal()
                        val body = call.receive<UserUpdateDto>()
                        val ans = userService.update(body, principal.id)
                        call.respond(ans)
                    }

                    get {
                        val principal = call.getPrincipal()
                        val ans = userService.findUserById(principal.id)
                        call.respond(ans)
                    }
                }
            }
        }
}
