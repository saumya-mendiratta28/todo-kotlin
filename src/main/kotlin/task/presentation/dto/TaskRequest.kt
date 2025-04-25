package org.example.task.presentation.dto


import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val title: String,
    val description: String? = null
)