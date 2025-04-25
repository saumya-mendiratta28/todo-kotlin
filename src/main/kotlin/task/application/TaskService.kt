package org.example.task.application

import org.example.task.domain.model.Task
import org.example.task.domain.repository.TaskRepository
import javax.inject.Inject


class TaskService @Inject constructor(
    private val repository: TaskRepository
) {
    fun getAllTasks(): List<Task> = repository.getAll()

    fun getTaskById(id: Int): Task? = repository.getById(id)

    fun createTask(task: Task): Task = repository.save(task)

    fun updateTask(task: Task): Task? {
        val existing = repository.getById(task.id) ?: return null
        val updated = existing.copy(
            title = task.title,
            description = task.description
        )
        return repository.update(task.id, updated)
    }

    fun deleteTask(id: Int): Boolean = repository.delete(id)
}