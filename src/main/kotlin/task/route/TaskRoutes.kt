package org.example.task.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.task.application.TaskService

fun Route.taskRoutes(taskService: TaskService) {
    route("/tasks") {
        get { taskService.getAll(call) }
        get("/{id}") { taskService.getById(call) }
        post { taskService.create(call) }
        put("/{id}") { taskService.update(call) }
        delete("/{id}") { taskService.delete(call) }
    }
}
