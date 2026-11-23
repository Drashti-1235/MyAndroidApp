package com.example.aistudymate.data.model

data class ChatMessage(
    val id: String = "",
    val message: String = "",
    val isUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)