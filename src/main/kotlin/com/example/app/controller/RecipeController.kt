package com.example.app.controller

import com.example.app.service.RecipeService
import com.example.plugins.config.Controller
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class RecipeController(
    private val recipeService: RecipeService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("recipes") {
                get("/{id}") {
                    val id = call.parameters.getOrFail<Long>("id")
                    val ans = recipeService.findRecipeById(id)
                    call.respond(ans)
                }
            }
        }
}
