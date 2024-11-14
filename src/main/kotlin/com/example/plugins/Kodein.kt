package com.example.plugins

import com.example.WebStarter
import com.example.app.controller.AuthController
import com.example.app.controller.FoodController
import com.example.app.repository.*
import com.example.app.repository.impl.*
import com.example.plugins.config.Controller
import com.example.app.service.AuthService
import com.example.app.service.FoodService
import com.example.app.service.UserService
import com.example.app.service.impl.AuthServiceImpl
import com.example.app.service.impl.FoodServiceImpl
import com.example.app.service.impl.UserServiceImpl
import com.example.plugins.config.AppConfig
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import org.kodein.di.*

internal val controllers = DI.Module("controllers") {
    bindSet<Controller> {
        bind { singleton { AuthController(instance()) } }
        bind { singleton { FoodController(instance()) } }
    }
}

internal val services = DI.Module("services") {
    bind<UserService>() with singleton { UserServiceImpl(instance()) }
    bind<AuthService>() with singleton { AuthServiceImpl(instance(), instance(), instance()) }
    bind<FoodService>() with singleton { FoodServiceImpl(instance(), instance(), instance()) }
}

internal val repositories = DI.Module("repositories") {
    bind<UserRepository>() with singleton { UserRepositoryImpl() }
    bind<PersonalDataRepository>() with singleton { PersonalDataRepositoryImpl() }
    bind<RecipeRepository>() with singleton { RecipeRepositoryImpl() }
    bind<IngredientRepository>() with singleton { IngredientRepositoryImpl() }
    bind<MealTimeRepository>() with singleton { MealTimeRepositoryImpl() }
}

val kodein = DI {

    import(controllers)
    import(services)
    import(repositories)

    bind { singleton { ConfigFactory.load().extract<AppConfig>("app") } }
    bind<WebStarter>() with singleton { WebStarter(instance(), instance()) }
}
