package org.example.task.domain.model

import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Task(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val createdAt: String
)
