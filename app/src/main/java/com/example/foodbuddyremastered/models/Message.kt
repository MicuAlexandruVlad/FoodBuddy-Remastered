package com.example.foodbuddyremastered.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Message")
open class Message: Serializable {

    companion object {
        const val TEXT_MESSAGE = 1
        const val IMAGE_MESSAGE = 2
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    lateinit var senderName: String
    lateinit var senderId: String
    lateinit var receiverId: String
    lateinit var timeSent: String
    lateinit var message: String

    // TODO: don't forget about this later on
    //var read = false

    lateinit var conversationId: String
    lateinit var ownerId: String

    var type: Int = -1
}