package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.example.config.DatabaseFactory
import org.example.di.DaggerAppComponent
import org.example.presentation.configureRouting


fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    DatabaseFactory.init()

    val appComponent = DaggerAppComponent.create()
    val taskService = appComponent.taskService()
    val subtaskService = appComponent.subtaskService()

    // Configure routing with services
    configureRouting(taskService, subtaskService)
}

