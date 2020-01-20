package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.MessageAdapter
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.TextMessage
import org.jetbrains.anko.find

class ChatViewModel(private val app: Application,
                    private val context: Context,
                    private val owner: LifecycleOwner)
    : ObservableViewModel(app) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var receiverName: TextView
    private lateinit var receiverPicture: ImageView
    private lateinit var messageField: EditText
    private lateinit var send: RelativeLayout

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: ArrayList<Message>


    fun initView() {
        val activity = context as Activity

        bindViews(activity)

        messages = ArrayList()
        messageAdapter = MessageAdapter(messages, context)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        insertTestMessage()
    }

    private fun insertTestMessage() {
        messages.add(TextMessage().apply {
            message = "Hey man !"
            timeSent = "2:31 PM"
        })

        messageAdapter.notifyItemInserted(messages.size - 1)
    }

    private fun bindViews(activity: Activity) {
        recyclerView = activity.find(R.id.rv_messages)
        receiverName = activity.find(R.id.tv_user_name)
        receiverPicture = activity.find(R.id.iv_user_photo)
        messageField = activity.find(R.id.et_message_text)
        send = activity.find(R.id.rl_send)
    }
}