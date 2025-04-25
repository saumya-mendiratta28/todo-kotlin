package org.example.config

import org.example.subtask.infrastructure.persistence.SubtaskTable
import org.example.task.infrastructure.persistence.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/tododb",
            driver = "org.postgresql.Driver",
            user = "saumya.mendiratta",
            password = "" // Empty password
        )

        transaction {
            SchemaUtils.create(TaskTable, SubtaskTable)
        }
    }
}