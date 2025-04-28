package org.example.task.infrastructure.persistence

import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.dao.id.IntIdTable


object TaskTable : IntIdTable("tasks") {
    val title = text("title")
    val description = text("description").nullable()
    val createdAt = datetime("created_at")
}