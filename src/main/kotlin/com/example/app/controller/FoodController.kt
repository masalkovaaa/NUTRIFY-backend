package com.example.app.controller

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.service.FoodService
import com.example.plugins.config.Controller
import com.example.plugins.extension.auth.getPrincipal
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.time.LocalDate

class FoodController(
    private val foodService: FoodService
) : Controller {

    companion object {
        val allowedExtensions = listOf("jpg", "jpeg", "png", "webp", "svg")
    }

    override val setup: Routing.() -> Unit
        get() = {
            route("food") {

                authenticate("admin") {
                    post {
                        val foodCreateDto = call.receive<FoodCreateDto>()
                        val result = foodService.save(foodCreateDto)
                        call.respond(result)
                    }

                    post("{recipeId}") {
                        val recipeId = call.parameters.getOrFail<Long>("recipeId")
                        val multipart = call.receiveMultipart()
                        multipart.forEachPart { part ->
                            if (part is PartData.FileItem) {
                                val filename = part.originalFileName ?: ""
                                val extension = filename.substringAfterLast('.', "").lowercase()

                                if (extension in allowedExtensions) {
                                    val bytes = part.provider().readRemaining().readByteArray()
                                    foodService.addImageToRecipe(bytes, recipeId)
                                    call.respond(HttpStatusCode.OK)
                                } else {
                                    part.dispose()
                                    call.respond(HttpStatusCode.BadRequest, "Unsupported file type: .$extension")
                                }
                            }
                            part.dispose()
                        }
                    }
                }

                authenticate("user") {
                    get {
                        try {
                            val mealType = call.queryParameters.getOrFail<MealType>("type")
                            call.respond(foodService.findByMealType(mealType))
                        } catch (e: Exception) {
                            call.respond(foodService.findAll())
                        }
                    }

                    get("diet") {
                        val date = call.queryParameters["date"].let { LocalDate.parse(it) }
                        val principal = call.getPrincipal()
                        call.respond(foodService.findDietByDate(date, principal.id))
                    }
                }
            }
        }
}
