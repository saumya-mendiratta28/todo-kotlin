package org.example.task.presentation.response

import kotlinx.serialization.Serializable
import org.example.task.domain.model.Task

@Serializable
data class TaskListResponse(val tasks: List<Task>)
