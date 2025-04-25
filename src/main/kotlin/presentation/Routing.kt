package org.example.presentation

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.subtask.application.SubtaskService
import org.example.subtask.presentation.subtaskRoutes
import org.example.task.application.TaskService
import org.example.task.presentation.taskRoutes

fun Application.configureRouting(
    taskService: TaskService,
    subtaskService: SubtaskService
) {
    routing {
        taskRoutes(taskService)
        subtaskRoutes(subtaskService)

        get("/") {
            call.respondText("Welcome to the TODO API built with DDD!")
        }
    }
}
