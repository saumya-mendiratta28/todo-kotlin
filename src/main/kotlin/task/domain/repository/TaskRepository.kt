package org.example.task.domain.repository

import org.example.task.domain.model.Task

interface TaskRepository {
    fun getAll(): List<Task>
    fun getById(id: Int): Task?
    fun save(task: Task): Task
    fun update(id: Int, task: Task): Task?
    fun delete(id: Int): Boolean
}