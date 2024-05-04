package com.example.demo.data

import java.time.OffsetDateTime

data class Todo(
    val id: Int,
    val title: String,
    val description: String = "",
    val isEditing: Boolean = false,
    val isFlagged: Boolean = false,
    val priority: Priority = Priority.NONE,
    val completedAt: OffsetDateTime? = null,
    val dueAt: OffsetDateTime? = null,
)

enum class Priority {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
    ;

    override fun toString(): String {
        return when (this) {
            NONE -> "None"
            LOW -> "Low"
            MEDIUM -> "Medium"
            HIGH -> "High"
        }
    }
}
