package com.example.aistudymate.data.model

data class Progress(
    val id: String = "",
    val userId: String = "",
    val totalQuizzes: Int = 0,
    val completedQuizzes: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val studyHours: Float = 0f,
    val updatedAt: Long = System.currentTimeMillis()
)