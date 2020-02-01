package com.example.foodbuddyremastered.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.database.Repository
import com.loopj.android.http.SyncHttpClient

class AuthViewModel: ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    lateinit var context: Context
    lateinit var currentUser: User

    // Dedicated to SplashActivity
    fun getLastAuthenticatedUser(): LocalUser? {
        Repository(context).apply {
            val list = getAuthenticatedUser(true)

            return if (list.size == 1 && list.isNotEmpty()) {
                list[0]
            } else {
                null
            }
        }
    }

    fun authUser(localUser: LocalUser, liveResponse: MutableLiveData<ResponseEvent>) {
        APIClient().authUserEmail(localUser.email, localUser.password, liveResponse, SyncHttpClient())
    }

    fun getConversationIds(ownerId: String): List<String> {
        return Repository(context).getConversationIds(ownerId)
    }

    fun getLastMessageForConversation(conversationId: String, ownerId: String): Message {
        return Repository(context).getLastMessageForConversation(conversationId, ownerId)
    }




    // Dedicated to WelcomeActivity
}