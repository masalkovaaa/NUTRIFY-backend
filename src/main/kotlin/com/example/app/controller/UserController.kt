package com.example.app.controller

import com.example.app.service.UserService
import com.example.plugins.config.Controller
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class UserController(
    private val userService: UserService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {

            route("users") {

                get {
                    val ans = userService.findAll()
                    call.respond(ans)
                }

                get("{id}") {
                    val id = call.parameters.getOrFail<Long>("id")
                    val ans = userService.findById(id)
                    call.respond(ans)
                }

            }

        }
}
