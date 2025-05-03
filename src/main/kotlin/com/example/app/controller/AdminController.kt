package com.example.app.controller

import com.example.app.service.impl.AdminService
import com.example.plugins.config.Controller
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class AdminController(
    private val adminService: AdminService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("admin") {
                authenticate("admin") {
                    get("{table}") {
                        val table = call.parameters.getOrFail("table")
                        val ans = adminService.findTableByName(table)
                        call.respond(ans)
                    }
                }
            }
        }
}