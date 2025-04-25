package org.example.task.infrastructure.repository


import org.example.task.domain.model.Task
import org.example.task.domain.repository.TaskRepository
import org.example.task.infrastructure.persistence.TaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class ExposedTaskRepository : TaskRepository {

    private fun resultRowToTask(row: ResultRow): Task = Task(
        id = row[TaskTable.id].value,
        title = row[TaskTable.title],
        description = row[TaskTable.description],
        createdAt = row[TaskTable.createdAt].toString()
    )

    override fun getAll(): List<Task> = transaction {
        TaskTable.selectAll().map(::resultRowToTask)
    }

    override fun getById(id: Int): Task? = transaction {
        TaskTable.select { TaskTable.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override fun save(task: Task): Task = transaction {
        val insertedId = TaskTable.insertAndGetId {
            it[title] = task.title
            it[description] = task.description
            it[createdAt] = DateTime.parse(task.createdAt)
        }
        task.copy(id = insertedId.value)
    }

    override fun update(id: Int, task: Task): Task? = transaction {
        val updatedRows = TaskTable.update({ TaskTable.id eq id }) {
            it[title] = task.title
            it[description] = task.description
            it[createdAt] = DateTime.parse(task.createdAt)
        }

        if (updatedRows > 0) getById(id) else null
    }

    override fun delete(id: Int): Boolean = transaction {
        TaskTable.deleteWhere { TaskTable.id eq id } > 0
    }
}
