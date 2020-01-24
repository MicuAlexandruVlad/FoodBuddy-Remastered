package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.User
import org.jetbrains.anko.find

class ConversationAdapter(private var items: ArrayList<Conversation>,
                          private var context: Context?,
                          private var currentUser: User
) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    private val TAG = "DiscoverAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.conversation_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = items[position]

        Glide.with(context!!).load(conversation.photoId).into(holder.image)
        holder.conversationName.text = conversation.conversationUserName
        holder.conversationTimestamp.text = conversation.lastMessage.timeSent
        holder.lastMessage.text = conversation.lastMessage.message
        holder.unreadMessages.text = conversation.unreadMessages.toString()

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.iv_conversation_photo)
        val conversationName: TextView = view.find(R.id.tv_conversation_user_name)
        val conversationTimestamp: TextView = view.find(R.id.tv_timestamp)
        val lastMessage: TextView = view.find(R.id.tv_last_message)
        val unreadMessages: TextView = view.find(R.id.tv_unread_messages)
    }
}