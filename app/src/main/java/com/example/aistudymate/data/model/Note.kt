package com.example.aistudymate.data.model

data class Note(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val subject: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)