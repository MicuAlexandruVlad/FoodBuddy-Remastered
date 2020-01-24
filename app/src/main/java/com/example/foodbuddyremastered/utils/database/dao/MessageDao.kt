package com.example.foodbuddyremastered.utils.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodbuddyremastered.models.Message

@Dao
interface MessageDao {

    @Insert
    fun insertMessage(message: Message)

    @Query("Select * From Message Where receiverId = :conversationId or senderId = :conversationId")
    fun getMessagesForConversation(conversationId: String): LiveData<List<Message>>

    @Query("Delete From Message")
    fun nuke()
}