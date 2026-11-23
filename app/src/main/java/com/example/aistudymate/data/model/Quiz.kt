package com.example.aistudymate.data.model

data class Quiz(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val questions: List<Question> = emptyList(),
    val totalQuestions: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

data class Question(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    val explanation: String = ""
)