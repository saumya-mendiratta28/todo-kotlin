package org.example.subtask.domain.repository

import org.example.subtask.domain.model.Subtask

interface SubtaskRepository {
    fun getAll(): List<Subtask>
    fun getById(id: Int): Subtask?
    fun getByTaskId(taskId: Int): List<Subtask>
    fun create(subtask: Subtask): Subtask
    fun update(id: Int, updated: Subtask): Boolean
    fun delete(id: Int): Boolean
}