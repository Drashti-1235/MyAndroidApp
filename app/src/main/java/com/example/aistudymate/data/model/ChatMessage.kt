
package com.example.aistudymate.data.model

data class ChatMessage(
    val id: Int = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
