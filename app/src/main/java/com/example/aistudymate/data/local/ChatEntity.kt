package com.example.aistudymate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val chatTitle: String,

    val messagesJson: String,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
)