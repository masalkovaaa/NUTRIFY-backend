package com.example.app.controller

import com.example.app.dto.user.PersonalDataUpdateDto
import com.example.app.service.PersonalDataService
import com.example.plugins.config.Controller
import com.example.plugins.extension.auth.getPrincipal
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class PersonalDataController(
    private val personalDataService: PersonalDataService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("personal_data") {
                authenticate("user") {
                    put {
                        val principal = call.getPrincipal()
                        val body = call.receive<PersonalDataUpdateDto>()
                        val ans = personalDataService.updatePersonalData(body, principal.id)
                        call.respond(ans)
                    }

                    get("is_possible_to_update") {
                        val principal = call.getPrincipal()
                        val ans = personalDataService.isPossibleToUpdate(principal.id)
                        call.respond(ans)
                    }
                }
            }
        }
}
