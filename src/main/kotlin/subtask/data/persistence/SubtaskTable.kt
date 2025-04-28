package org.example.subtask.infrastructure.persistence

import org.example.task.infrastructure.persistence.TaskTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SubtaskTable : IntIdTable("subtasks") {
    val taskId = reference("task_id", TaskTable, onDelete = ReferenceOption.CASCADE)
    val title = text("title")
    val done = bool("done").default(false)
    val createdAt = datetime("created_at")
}
