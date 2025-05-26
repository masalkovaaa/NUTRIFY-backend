package com.example.app.controller

import com.example.app.model.Ingredient
import com.example.app.model.Recipe
import com.example.app.service.RecipeService
import com.example.plugins.config.Controller
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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

                authenticate("admin") {
                    patch {
                        val dto = call.receive<Ingredient>()
                        recipeService.updateIngredient(dto)
                        call.respond(HttpStatusCode.OK)
                    }

                    put {
                        val dto = call.receive<Recipe>()
                        recipeService.updateRecipe(dto)
                        call.respond(HttpStatusCode.OK)
                    }
                }

            }
        }
}
