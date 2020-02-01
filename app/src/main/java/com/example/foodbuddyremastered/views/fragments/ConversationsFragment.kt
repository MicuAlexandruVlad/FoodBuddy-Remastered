package com.example.foodbuddyremastered.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ConversationAdapter
import com.example.foodbuddyremastered.constants.ButtonIds
import com.example.foodbuddyremastered.events.ButtonPressedEvent
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.viewmodels.MainActivityViewModel
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread
import java.lang.Exception

class ConversationsFragment(private val c: Context) : Fragment() {
    companion object {
        const val TAG = "ConversationsFragment"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var switchFragment: LinearLayout

    private lateinit var conversations: ArrayList<Conversation>
    private lateinit var conversationIds: ArrayList<String>
    private lateinit var currentUser: User
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conversations = arguments!!.getSerializable("conversations") as ArrayList<Conversation>
        currentUser = arguments!!.getSerializable("currentUser") as User
        conversationIds = arguments!!.getStringArrayList("conversationIds") as ArrayList<String>
        conversationAdapter = ConversationAdapter(conversations, context, User())


        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        } ?: throw Exception("Invalid Activity") as Throwable

        viewModel.context = context!!

        // listen for new messages (received or sent)
        Repository(context!!).getLastMessageLive(currentUser.id).observe(
            this, Observer<Message> { message ->
                if (message != null) {
                    if (message.conversationId in conversationIds) {
                        updateConversationLastMessage(message)
                    } else {
                        // TODO: create a new conversation (new entry in the conversations array and get the data for that user)
                    }
                }
            }
        )
    }

    private fun updateConversationLastMessage(message: Message) {
        for (index in conversations.indices) {
            if (conversations[index].conversationId.compareTo(message.conversationId) == 0) {
                conversations[index].lastMessage = message
                runOnUiThread { conversationAdapter.notifyItemChanged(index) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_conversations, container, false)

        bindViews(view)

        recyclerView.adapter = conversationAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        switchFragment.setOnClickListener {
            EventBus.getDefault().post(ButtonPressedEvent().also {
                it.buttonId = ButtonIds.NAV_TO_DISCOVER
            })
        }

        //testUi()

        return view
    }

    private fun testUi() {
        conversations.add(Conversation().also {
            it.photoId = "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            it.conversationId = ""
            it.conversationUserName = "Dan John"
            it.unreadMessages = 2
            it.conversationUser = User().also { user ->
                user.firstName = "Dan"
                user.lastName = "John"
            }
            it.lastMessage = Message().also { message ->
                message.message = "Hey ! Wanna hang out ?"
                message.timeSent = "12:45 AM"
            }
        })

        conversationAdapter.notifyItemInserted(conversations.size - 1)
    }

    private fun bindViews(view: View) {
        recyclerView = view.find(R.id.rv_conversations)
        switchFragment = view.find(R.id.ll_next_fragment)
    }


}