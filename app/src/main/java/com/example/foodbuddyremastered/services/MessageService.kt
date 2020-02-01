package com.example.foodbuddyremastered.services

import android.util.Log
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.utils.JsonUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class MessageService: FirebaseMessagingService() {

    companion object {
        const val TAG = "MessageService"
    }

    private var lastAuthenticatedUser: LocalUser? = null



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Remote message -> ${remoteMessage.data}")

        val message = JsonUtils.jsonToMessage(remoteMessage.data)

        if (lastAuthenticatedUser == null) {
            getAuthenticatedUser(message)
        } else {
            storeMessage(message)
        }
    }

    private fun getAuthenticatedUser(message: Message) {
        doAsync {
            Repository(this@MessageService).apply {
                val list = this.getAuthenticatedUserLive(true)

                list.observeOn(Schedulers.computation())
                    .subscribe {
                        Log.d(TAG, "We got something boyz -> ${Gson().toJson(it)}")

                        if (it.isNotEmpty() && it.size == 1) {
                            lastAuthenticatedUser = it[0]
                            storeMessage(message)
                        }
                    }
            }
        }
    }

    private fun storeMessage(message: Message) {
        message.ownerId = lastAuthenticatedUser!!.userId

        Repository(this).apply {
            insertMessage(message)
        }
    }
}
