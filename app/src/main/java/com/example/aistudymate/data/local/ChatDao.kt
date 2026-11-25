package com.example.aistudymate.data.local



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: ChatEntity): Long

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Query("SELECT * FROM chat_history ORDER BY updatedAt DESC")
    suspend fun getAllChats(): List<ChatEntity>

    @Query("SELECT * FROM chat_history WHERE id = :chatId")
    suspend fun getChatById(chatId: Int): ChatEntity?

    @Query("DELETE FROM chat_history WHERE id = :chatId")
    suspend fun deleteChat(chatId: Int)

    @Query("DELETE FROM chat_history")
    suspend fun deleteAllChats()
}