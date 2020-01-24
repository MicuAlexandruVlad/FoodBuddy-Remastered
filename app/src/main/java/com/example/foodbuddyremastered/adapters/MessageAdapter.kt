package com.example.foodbuddyremastered.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.ObjectTypes
import com.example.foodbuddyremastered.models.Message
import org.jetbrains.anko.find
import java.util.*

class MessageAdapter(private var items: LinkedList<Message>,
                     private var context: Context?,
                     private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MessageAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val textMessageSentView = LayoutInflater.from(p0.context).inflate(R.layout.message_sent,
            p0, false)

        val textMessageReceivedView = LayoutInflater.from(p0.context).inflate(R.layout.message_received,
            p0, false)

        if (p1 == ObjectTypes.TEXT_MESSAGE_RECEIVED)
            return TextMessageReceivedHolder(textMessageReceivedView)

        return TextMessageSentHolder(textMessageSentView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = items[position]
        if (message.senderId.compareTo(currentUserId) == 0) {
            if (message.type == Message.TEXT_MESSAGE) {
                return ObjectTypes.TEXT_MESSAGE_SENT
            }
        }

        return ObjectTypes.TEXT_MESSAGE_RECEIVED
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            ObjectTypes.TEXT_MESSAGE_SENT -> {
                val h = holder as TextMessageSentHolder

                val textMessage = items[position]

                h.messageText.text = textMessage.message
                h.timestamp.text = textMessage.timeSent
            }

            ObjectTypes.TEXT_MESSAGE_RECEIVED -> {
                val h = holder as TextMessageReceivedHolder

                val textMessage = items[position]

                h.messageText.text = textMessage.message
                h.timestamp.text = textMessage.timeSent
            }
        }
    }

    class TextMessageSentHolder (view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.find(R.id.tv_message_text)
        val timestamp: TextView = view.find(R.id.tv_timestamp)
    }

    class TextMessageReceivedHolder (view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.find(R.id.tv_message_text)
        val timestamp: TextView = view.find(R.id.tv_timestamp)
    }
}