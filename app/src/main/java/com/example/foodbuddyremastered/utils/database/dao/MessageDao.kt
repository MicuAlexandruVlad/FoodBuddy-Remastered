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

    @Query("Select * From Message Where conversationId = :conversationId and ownerId = :ownerId")
    fun getMessagesForConversationLive(conversationId: String, ownerId: String): LiveData<List<Message>>

    @Query("Select * From Message Where conversationId = :conversationId and ownerId = :ownerId")
    fun getMessagesForConversation(conversationId: String, ownerId: String): List<Message>

    @Query("""Select Distinct conversationId From Message Where ownerId = :ownerId
            and conversationId != :ownerId""")
    fun getConversationIds(ownerId: String): List<String>

    @Query("""Select * From Message Where ownerId = :ownerId and conversationId = :conversationId
        order by id desc limit 1
    """)
    fun getLastMessageForConversation(conversationId: String, ownerId: String): Message

    @Query("""Select * From Message Where ownerId = :ownerId
        order by id desc limit 1
    """)
    fun getLastMessageLive(ownerId: String): LiveData<Message>

    @Query("Delete From Message")
    fun nuke()
}