package com.example.aistudymate.data.model

data class StudyPlan(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val subject: String = "",
    val description: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isCompleted: Boolean = false
)