package com.example.foodbuddyremastered.models

import java.io.Serializable

class Conversation: Serializable {

    // conversationId is also the user ID

    lateinit var conversationId: String
    lateinit var photoId: String
    lateinit var conversationUser: User
    lateinit var conversationUserName: String
    lateinit var lastMessage: Message
    var unreadMessages: Int = 0
}