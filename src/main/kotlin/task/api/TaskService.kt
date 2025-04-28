package org.example.task.application

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.example.task.domain.model.Task
import org.example.task.domain.usecase.TaskUseCase
import org.example.task.presentation.dto.TaskRequest
import org.example.task.presentation.response.TaskListResponse
import org.joda.time.DateTime
import javax.inject.Inject


class TaskService @Inject constructor(
    private val useCase: TaskUseCase
) {
    suspend fun getAll(call: ApplicationCall) {
        val tasks = useCase.getAll()
        call.respond(TaskListResponse(tasks))
    }

    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val task = useCase.getById(id)
        if (task != null) {
            call.respond(task)
        } else {
            call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val request = call.receive<TaskRequest>()
            val task = Task(
                id = 0,
                title = request.title,
                description = request.description,
                createdAt = DateTime.now().toString()
            )
            val saved = useCase.save(task)
            call.respond(HttpStatusCode.Created, saved)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request: ${e.message}")
        }
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        try {
            val request = call.receive<TaskRequest>()
            val existing = useCase.getById(id)
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return
            }

            val updated = existing.copy(
                title = request.title,
                description = request.description
            )

            val result = useCase.update(id, updated)
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Could not update task")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request: ${e.message}")
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val deleted = useCase.delete(id)
        if (deleted) {
            call.respond(HttpStatusCode.OK, "Task deleted successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }
}
