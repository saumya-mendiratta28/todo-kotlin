package org.example.subtask.application

import io.ktor.server.plugins.*
import org.example.subtask.domain.model.Subtask
import org.example.subtask.domain.repository.SubtaskRepository
import org.example.task.domain.repository.TaskRepository
import javax.inject.Inject

class SubtaskService @Inject constructor(
    private val repository: SubtaskRepository,
    private val taskRepository: TaskRepository
) {
    fun getAllSubtasks(): List<Subtask> = repository.getAll()

    fun getSubtaskById(id: Int): Subtask? = repository.getById(id)

    fun getSubtasksByTaskId(taskId: Int): List<Subtask> = repository.getByTaskId(taskId)

    fun createSubtask(subtask: Subtask): Subtask {
        val parent = taskRepository.getById(subtask.taskId)
            ?: throw NotFoundException("Task with id=${subtask.taskId} not found")
        return repository.create(subtask)
    }

    fun updateSubtask(id: Int, updated: Subtask): Boolean {
        repository.getById(id) ?: throw NotFoundException("Subtask with id=$id not found")
        return repository.update(id, updated)
    }

    fun deleteSubtask(id: Int): Boolean {
        repository.getById(id) ?: throw NotFoundException("Subtask with id=$id not found")
        return repository.delete(id)
    }
}
