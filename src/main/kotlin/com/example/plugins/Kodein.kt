package com.example.plugins

import com.example.WebStarter
import com.example.app.controller.AuthController
import com.example.plugins.config.Controller
import com.example.app.controller.UserController
import com.example.app.repository.PersonalDataRepository
import com.example.app.repository.UserRepository
import com.example.app.repository.impl.PersonalDataRepositoryImpl
import com.example.app.repository.impl.UserRepositoryImpl
import com.example.app.service.AuthService
import com.example.app.service.UserService
import com.example.app.service.impl.AuthServiceImpl
import com.example.app.service.impl.UserServiceImpl
import com.example.plugins.config.AppConfig
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import org.kodein.di.*

internal val controllers = DI.Module("controllers") {
    bindSet<Controller> {
        bind { singleton { UserController(instance()) } }
        bind { singleton { AuthController(instance()) } }
    }
}

internal val services = DI.Module("services") {
    bind<UserService>() with singleton { UserServiceImpl(instance()) }
    bind<AuthService>() with singleton { AuthServiceImpl(instance(), instance(), instance()) }
}

internal val repositories = DI.Module("repositories") {
    bind<UserRepository>() with singleton { UserRepositoryImpl() }
    bind<PersonalDataRepository>() with singleton { PersonalDataRepositoryImpl() }
}

val kodein = DI {

    import(controllers)
    import(services)
    import(repositories)

    bind { singleton { ConfigFactory.load().extract<AppConfig>("app") } }
    bind<WebStarter>() with singleton { WebStarter(instance(), instance()) }
}
