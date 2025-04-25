package org.example.subtask.presentation.request


import kotlinx.serialization.Serializable

@Serializable
data class SubtaskRequest(
    val taskId: Int,
    val title: String,
    val done: Boolean = false
)