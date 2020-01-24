package com.example.foodbuddyremastered.services

import android.util.Log
import com.example.foodbuddyremastered.utils.JsonUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageService: FirebaseMessagingService() {

    companion object {
        const val TAG = "MessageService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Remote message -> ${remoteMessage.data}")

        val message = JsonUtils.jsonToMessage(remoteMessage.data)

        Repository(this).apply {
            insertMessage(message)
        }
    }
}