package com.example.app.controller

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.service.FoodService
import com.example.plugins.config.Controller
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class FoodController(
    private val foodService: FoodService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("food") {
                authenticate("admin") {
                    post {
                        val foodCreateDto = call.receive<FoodCreateDto>()
                        foodService.save(foodCreateDto)
                        call.respond(HttpStatusCode.Created)
                    }
                }

                get {
                    val mealType = call.queryParameters.getOrFail<MealType>("type")
                    val ans = foodService.findByMealType(mealType)
                    call.respond(ans)
                }
            }
        }
}
