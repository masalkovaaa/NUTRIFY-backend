package com.example

import com.example.plugins.config.AppConfig
import com.example.plugins.config.Controller
import com.example.plugins.config.HttpServerConfig
import com.example.plugins.kodein
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.instance

fun main() {
    val starter by kodein.instance<WebStarter>()
    starter.start()
}

class WebStarter(
    private val appConfig: AppConfig,
    private val controllers: Set<Controller>
) {
    fun start() {
        embeddedServer(Netty, 8080) {
            HttpServerConfig.configure(this, appConfig, controllers)
        }.start(wait = true)
    }
}
