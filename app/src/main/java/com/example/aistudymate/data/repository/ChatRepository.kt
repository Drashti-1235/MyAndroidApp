package com.example.aistudymate.data.repository

class ChatRepository {

    private val messages =
        mutableListOf<ChatMessage>()

    fun sendMessage(message: ChatMessage) {
        messages.add(message)
    }

    fun getAllMessages(): List<ChatMessage> {
        return messages.toList()
    }

    fun clearChat() {
        messages.clear()
    }

    fun getMessageCount(): Int {
        return messages.size
    }
}