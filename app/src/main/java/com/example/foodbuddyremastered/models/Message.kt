package com.example.foodbuddyremastered.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Message")
open class Message {

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

    var type: Int = -1
}