package org.example.subtask.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Subtask(
    val id: Int,
    val taskId: Int,
    val title: String,
    val done: Boolean,
    val createdAt: String
)