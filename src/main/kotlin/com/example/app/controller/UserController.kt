package com.example.app.controller

import com.example.app.service.UserService
import com.example.plugins.config.Controller
import io.ktor.server.routing.*

class UserController(
    private val userService: UserService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {

            route("users") {


            }

        }
}
