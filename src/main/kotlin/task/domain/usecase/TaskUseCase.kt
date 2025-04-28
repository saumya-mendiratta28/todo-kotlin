package org.example.task.domain.usecase

import org.example.task.domain.model.Task
import org.example.task.domain.repository.TaskRepository
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    fun getAll(): List<Task> = repository.getAll()
    fun getById(id: Int): Task? = repository.getById(id)
    fun save(task: Task): Task = repository.save(task)
    fun update(id: Int, task: Task): Task? = repository.update(id, task)
    fun delete(id: Int): Boolean = repository.delete(id)
}
