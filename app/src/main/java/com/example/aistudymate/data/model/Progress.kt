
package com.example.aistudymate.data.model

data class Progress(
    val id: Int = 0,
    val userId: String,
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val studyTimeMinutes: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
