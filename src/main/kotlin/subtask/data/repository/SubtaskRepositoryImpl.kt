package org.example.subtask.infrastructure.repository


import org.example.subtask.domain.model.Subtask
import org.example.subtask.domain.repository.SubtaskRepository
import org.example.subtask.infrastructure.persistence.SubtaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class SubtaskRepositoryImpl : SubtaskRepository {

    private fun resultRowToSubtask(row: ResultRow): Subtask {
        return Subtask(
            id = row[SubtaskTable.id].value,
            taskId = row[SubtaskTable.taskId].value,
            title = row[SubtaskTable.title],
            done = row[SubtaskTable.done],
            createdAt = row[SubtaskTable.createdAt].toString()
        )
    }

    override fun getAll(): List<Subtask> = transaction {
        SubtaskTable.selectAll().map(::resultRowToSubtask)
    }

    override fun getById(id: Int): Subtask? = transaction {
        SubtaskTable.select { SubtaskTable.id eq id }
            .map(::resultRowToSubtask)
            .singleOrNull()
    }

    override fun getByTaskId(taskId: Int): List<Subtask> = transaction {
        SubtaskTable.select { SubtaskTable.taskId eq taskId }
            .map(::resultRowToSubtask)
    }

    override fun create(subtask: Subtask): Subtask = transaction {
        val insertedId = SubtaskTable.insertAndGetId {
            it[title] = subtask.title
            it[done] = subtask.done
            it[taskId] = subtask.taskId
            it[createdAt] = DateTime.parse(subtask.createdAt)
        }

        subtask.copy(id = insertedId.value)
    }

    override fun update(id: Int, updated: Subtask): Boolean = transaction {
        SubtaskTable.update({ SubtaskTable.id eq id }) {
            it[title] = updated.title
            it[done] = updated.done
            it[taskId] = updated.taskId
            it[createdAt] = DateTime.parse(updated.createdAt)
        } > 0
    }

    override fun delete(id: Int): Boolean = transaction {
        SubtaskTable.deleteWhere { SubtaskTable.id eq id } > 0
    }
}
