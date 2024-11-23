package com.example.app.controller

import com.example.app.dto.food.FoodCreateDto
import com.example.app.model.MealType
import com.example.app.model.Recipe
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

class FoodController(
    private val foodService: FoodService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            route("food") {

                post("test") {
                    val body = call.receive<List<List<Recipe>>>()
                    val calories = body.map { it.map { it.calories }.sum() }
                    val protein = body.map { it.map { it.protein }.sum() }
                    val fats = body.map { it.map { it.fats }.sum() }
                    val carbs = body.map { it.map { it.carbs }.sum() }
                    println("$calories $protein $fats $carbs")
                    call.respond(HttpStatusCode.OK)
                }
                authenticate("admin") {
                    post {
                        val foodCreateDto = call.receive<FoodCreateDto>()
                        foodService.save(foodCreateDto)
                        call.respond(HttpStatusCode.Created)
                    }

                    post("{recipeId}") {
                        val recipeId = call.parameters.getOrFail<Long>("recipeId")
                        val multipart = call.receiveMultipart()
                        multipart.forEachPart { part ->
                            if (part is PartData.FileItem) {
                                val bytes = part.provider().readRemaining().readByteArray()
                                foodService.addImageToRecipe(bytes, recipeId)
                            }
                            part.dispose()
                        }
                        call.respond(HttpStatusCode.OK)
                    }
                }

                authenticate("user") {
                    get {
                        val principal = call.getPrincipal()
                        val ans = foodService.calculateDiet(principal.id)
                        call.respond(ans)
                    }

//                    ans.map {
//                        listOf(it.map { it.id }.joinToString("-")) to
//                                listOf(
//                                    it.map { it.calories }.sum(),
//                                    it.map { it.protein }.sum(),
//                                    it.map { it.fats }.sum(),
//                                    it.map { it.carbs }.sum()
//                                ).joinToString("-")
//                    }
                }

                get {
                    val mealType = call.queryParameters.getOrFail<MealType>("type")
                    val ans = foodService.findByMealType(mealType)
                    call.respond(ans)
                }
            }
        }
}
