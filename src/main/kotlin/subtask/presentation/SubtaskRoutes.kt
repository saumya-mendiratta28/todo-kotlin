package org.example.subtask.presentation


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.subtask.application.SubtaskService
import org.example.subtask.domain.model.Subtask
import org.example.subtask.presentation.request.SubtaskRequest
import org.joda.time.DateTime

fun Route.subtaskRoutes(subtaskService: SubtaskService) {

    route("/subtasks") {

        get {
            val subtasks = subtaskService.getAllSubtasks()
            call.respond(HttpStatusCode.OK, subtasks)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                return@get
            }

            val subtask = subtaskService.getSubtaskById(id)
            if (subtask != null) {
                call.respond(HttpStatusCode.OK, subtask)
            } else {
                call.respond(HttpStatusCode.NotFound, "Subtask not found")
            }
        }

        get("/task/{taskId}") {
            val taskId = call.parameters["taskId"]?.toIntOrNull()
            if (taskId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing taskId")
                return@get
            }

            val subtasks = subtaskService.getSubtasksByTaskId(taskId)
            call.respond(HttpStatusCode.OK, subtasks)
        }

        post {
            val req = call.receive<SubtaskRequest>()
            val dto = Subtask(
                id = 0,
                taskId = req.taskId,
                title = req.title,
                done = req.done,
                createdAt = DateTime.now().toString()
            )

            val created = subtaskService.createSubtask(dto)
            call.respond(HttpStatusCode.Created, created)
        }


        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                return@put
            }

            // 1. Receive the DTO
            val req = call.receive<SubtaskRequest>()

            // 2. Fetch existing domain object
            val existing = subtaskService.getSubtaskById(id)
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Subtask not found")
                return@put
            }

            // 3. Create updated domain model
            val updatedModel = existing.copy(
                title = req.title,
                done = req.done
                // taskId unchanged; createdAt unchanged
            )

            // 4. Perform the update
            val success = subtaskService.updateSubtask(id, updatedModel)
            if (success) {
                call.respond(HttpStatusCode.OK, updatedModel)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Could not update subtask")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                return@delete
            }

            val deleted = subtaskService.deleteSubtask(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Subtask deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Subtask not found")
            }
        }
    }
}
