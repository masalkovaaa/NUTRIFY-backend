package com.example.plugins.config

import io.ktor.server.routing.*

interface Controller {
    val setup: (Routing.() -> Unit)
}
