package com.example.foodbuddyremastered.models

import java.io.Serializable

//@Entity(tableName = "Conversation")
class Conversation: Serializable {

    // conversationId is also the user ID

    //@PrimaryKey(autoGenerate = true)
    //var id: Int? = null

    lateinit var conversationId: String
    lateinit var photoId: String

    //@Ignore
    lateinit var conversationUser: User
    lateinit var conversationUserName: String

    lateinit var lastMessage: Message
    var unreadMessages: Int = 0
}