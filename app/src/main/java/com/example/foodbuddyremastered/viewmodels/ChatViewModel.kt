package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.MessageAdapter
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.utils.database.TimeUtils
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import java.util.*


class ChatViewModel(private val app: Application,
                    private val context: Context,
                    private val owner: LifecycleOwner,
                    private val currentUser: User,
                    private val conversationUser: User
)
    : ObservableViewModel(app) {

    companion object {
        const val TAG = "ChatViewModel"
    }

    private lateinit var parent: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var receiverName: TextView
    private lateinit var receiverPicture: ImageView
    private lateinit var messageField: EditText
    private lateinit var send: RelativeLayout

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: LinkedList<Message>
    private var notifUtils = NotifUtils(context)
    private lateinit var layoutManager: LinearLayoutManager

    var client = APIClient()

    var messageValue = ""

    fun initView() {
        val activity = context as Activity

        client.initSocket()

        bindViews(activity)

        //doAsync { Repository(context).apply { nukeMessages() } }

        messages = LinkedList()
        messageAdapter = MessageAdapter(messages, context, currentUser.id)
        recyclerView.adapter = messageAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        getMessagesForConversation(conversationUser.id)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    // TODO: Load another batch of messages from RoomDB
                }
            }
        })
    }

    private fun bindViews(activity: Activity) {
        parent = activity.find(R.id.rl_parent)
        recyclerView = activity.find(R.id.rv_messages)
        receiverName = activity.find(R.id.tv_user_name)
        receiverPicture = activity.find(R.id.iv_user_photo)
        messageField = activity.find(R.id.et_message_text)
        send = activity.find(R.id.rl_send)
    }

    fun onSendTextMessage() {
        if (messageValue.isEmpty()) {
            notifUtils.createToast("Message field is empty").show()
        } else {
            messages.add(Message().apply {
                message = messageValue
                timeSent = TimeUtils.getCurrentHour()
                senderId = currentUser.id
                senderName = with(currentUser) { "$firstName $lastName" }
                receiverId = conversationUser.id
                type = Message.TEXT_MESSAGE
                ownerId = currentUser.id
                conversationId = conversationUser.id

                storeMessageInRoom(this)
                doAsync { client.emitTextMessage(this@apply) }

                Log.d(TAG, "onSendTextMessage: Message added -> $message")
            })

            Log.d(TAG, "onSendTextMessage: Messages -> ${Gson().toJson(messages)}")

            messageAdapter.notifyItemInserted(messages.size - 1)

            Log.d(TAG, "onSendTextMessage: Adapter item count -> ${messageAdapter.itemCount}")
            scrollToBottom()

            messageField.setText("")
        }
    }

    private fun storeMessageInRoom(message: Message) {
        doAsync {
            Repository(context).apply {
                insertMessage(message)
            }
        }
    }

    private fun getMessagesForConversation(id: String) {
        Repository(context).getMessagesForConversationLive(id, currentUser.id)
            .observe(owner, Observer<List<Message>> {
                if (messages.isNotEmpty())
                    messages.clear()
                messages.addAll(it)
                messageAdapter.notifyDataSetChanged()
                scrollToBottom()
            })
    }

    private fun scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(messageAdapter.itemCount - 1, 0)
    }


}