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
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.TextMessage
import com.example.foodbuddyremastered.models.User
import org.jetbrains.anko.find

class MessageAdapter(private var items: ArrayList<Message>,
                     private var context: Context?
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val TAG = "MessageAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.message_sent,
            p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = items[position]

        holder.messageText.text = (message as TextMessage).message
        holder.timestamp.text = message.timeSent

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.find(R.id.tv_message_text)
        val timestamp: TextView = view.find(R.id.tv_timestamp)
    }
}