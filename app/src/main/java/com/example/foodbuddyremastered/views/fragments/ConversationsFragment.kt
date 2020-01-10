package com.example.foodbuddyremastered.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ConversationAdapter
import com.example.foodbuddyremastered.constants.ButtonIds
import com.example.foodbuddyremastered.events.ButtonPressedEvent
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.TextMessage
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.MainActivityViewModel
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import java.lang.Exception

class ConversationsFragment: Fragment() {
    companion object {
        const val TAG = "ConversationsFragment"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var switchFragment: LinearLayout

    private lateinit var list: ArrayList<Conversation>

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = ArrayList()
        conversationAdapter = ConversationAdapter(list, context, User())

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        } ?: throw Exception("Invalid Activity") as Throwable
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

        testUi()

        return view
    }

    private fun testUi() {
        list.add(Conversation().also {
            it.photoId = "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            it.conversationId = ""
            it.conversationUserName = "Dan John"
            it.unreadMessages = 2
            it.conversationUser = User().also { user ->
                user.firstName = "Dan"
                user.lastName = "John"
            }
            it.lastMessage = TextMessage().also { message ->
                message.message = "Hey ! Wanna hang out ?"
                message.timeSent = "12:45 AM"
            }
        })

        conversationAdapter.notifyItemInserted(list.size - 1)
    }

    private fun bindViews(view: View) {
        recyclerView = view.find(R.id.rv_conversations)
        switchFragment = view.find(R.id.ll_next_fragment)
    }


}