package com.example.foodbuddyremastered.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.viewmodels.AuthViewModel
import com.google.gson.Gson
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class SplashActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val liveResponse = MutableLiveData<ResponseEvent>()
        val conversationUsers = MutableLiveData<ResponseEvent>()
        val conversations = ArrayList<Conversation>()
        val conversationIds = ArrayList<String>()



        viewModel = ViewModelProviders.of(this)[AuthViewModel::class.java]
        viewModel.context = this

        conversationUsers.observe(this, Observer<ResponseEvent> { event ->
            when (event.status) {
                HttpStatus.SC_OK -> {
                    val users = event.payload as ArrayList<*>

                    Log.d(TAG, "conversationUsers.observe: Users -> ${ Gson().toJson(users)}")

                    for (conversation in conversations) {
                        for (user in users) {
                            user as User
                            if (user.id.compareTo(conversation.conversationId) == 0) {
                                conversation.conversationUser = user
                                conversation.conversationUserName = "${user.firstName} ${user.lastName}"
                                conversation.photoId = user.photoId
                            }
                        }
                    }

                    Log.d(TAG, "liveResponse.observe: Conversation list -> " +
                            Gson().toJson(conversations)
                    )

                    startActivity(Intent(this@SplashActivity,
                        MainActivity::class.java).apply {
                        putExtra("currentUser", currentUser)
                        putExtra("conversations", conversations)
                        putExtra("conversationIds", conversationIds)
                        // TODO: get and send a batch of discovered users discovered by the default filter
                    })
                    finish()
                }

                else -> {
                    Log.d(TAG, "conversationUsers.observe: No users found")
                }
            }
        })

        liveResponse.observe(this, Observer<ResponseEvent> { event ->
            when (event.status) {
                HttpStatus.SC_OK -> {
                    currentUser = event.payload as User


                    doAsync {
                        conversationIds.addAll(viewModel.getConversationIds(currentUser.id))
                        Log.d(TAG, "liveResponse.observe: Conversation Ids -> " +
                                Gson().toJson(conversationIds)
                        )

                        if (conversationIds.isNotEmpty()) {
                            conversations.apply {
                                for (id in conversationIds) {
                                    add(Conversation().apply {
                                        this.lastMessage = viewModel.getLastMessageForConversation(
                                            id,
                                            currentUser.id
                                        )
                                        this.conversationId = id
                                        this.unreadMessages = 2
                                    })
                                }
                            }

                            APIClient().getConversationUsers(
                                conversationIds,
                                conversationUsers,
                                SyncHttpClient()
                            )
                        } else {
                            startActivity(Intent(this@SplashActivity,
                                MainActivity::class.java).apply {
                                putExtra("currentUser", currentUser)
                                putExtra("conversations", conversations)
                                putExtra("conversationIds", conversationIds)
                                // TODO: get and send a batch of discovered users discovered by the default filter
                            })
                        }
                    }
                }

                else -> {
                    NotifUtils(this@SplashActivity).createToast("User not found").show()
                }
            }
        })

        Handler().apply {
            postDelayed({
                val pBar = find<ProgressBar>(R.id.pb_progress)

                pBar.visibility = View.VISIBLE
                // TODO: Move this outside of the handler
                doAsync {
                    val localUser = viewModel.getLastAuthenticatedUser()

                    Log.d(TAG, "Local user -> ${Gson().toJson(localUser)}")

                    if (localUser != null) {
                        viewModel.authUser(localUser, liveResponse)
                    } else {
                        startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                        finish()
                    }
                }
            }, 800)
        }
    }
}
