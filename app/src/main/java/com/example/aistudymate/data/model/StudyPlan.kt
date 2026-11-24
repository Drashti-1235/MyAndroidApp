
package com.example.aistudymate.data.model

data class StudyPlan(
    val id: String,
    val title: String,
    val description: String = "",
    val subject: String = "",
    val durationMinutes: Int = 60,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
