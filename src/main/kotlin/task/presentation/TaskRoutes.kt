package org.example.task.presentation

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.task.application.TaskService
import org.example.task.domain.model.Task
import org.joda.time.DateTime
import io.ktor.http.*
import org.example.task.presentation.dto.TaskRequest
import org.example.task.presentation.response.TaskListResponse

fun Route.taskRoutes(taskService: TaskService) {

    route("/tasks") {

        get {
            val tasks = taskService.getAllTasks()
            call.respond(TaskListResponse(tasks))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@get
            }

            val task = taskService.getTaskById(id)
            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }

        post {
            val request = call.receive<TaskRequest>()
            val createdTask = taskService.createTask(
                Task(
                    id = 0,
                    title = request.title,
                    description = request.description,
                    createdAt = DateTime.now().toString() // convert to String
                )
            )
            call.respond(HttpStatusCode.Created, createdTask)
        }


        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                return@put
            }

            val req = call.receive<TaskRequest>()
            // Fetch existing task
            val existing = taskService.getTaskById(id)
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return@put
            }

            // Create a new Task model with updated fields
            val updatedModel = existing.copy(
                title = req.title,
                description = req.description
                // createdAt stays the same
            )

            // Pass that to your service/repo update
            val updated = taskService.updateTask(updatedModel)
            if (updated != null) {
                call.respond(HttpStatusCode.OK, updated)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Could not update task")
            }
        }


        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@delete
            }

            val deleted = taskService.deleteTask(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Task deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
    }
}
