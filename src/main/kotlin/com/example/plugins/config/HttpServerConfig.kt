package com.example.plugins.config

import com.example.plugins.configureCors
import com.masalkova.plugins.configureDatabases
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.routing.*

object HttpServerConfig {

    fun configure(
        application: Application,
        appConfig: AppConfig,
        controllers: Set<Controller>
    ) {
        application.apply {
            configureSecurity(appConfig)
            configureSerialization()
            configureStatusPages()
            configureCors()
            configureDatabases(appConfig)
            controllers.forEach { routing(it.setup) }
        }
    }
}
