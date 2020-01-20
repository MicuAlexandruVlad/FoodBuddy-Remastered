package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.views.ChatActivity
import org.jetbrains.anko.find

class DiscoverAdapter(private var items: ArrayList<User>,
                      private var context: Context?,
                      private var currentUser: User
) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    private val TAG = "DiscoverAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.discover_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]

        holder.userName.text = "${ user.firstName } ${ user.lastName }"
        holder.age.text = "${ user.age } years"
        holder.city.text = user.city

        holder.parent.setOnClickListener {
            Intent(context, ChatActivity::class.java).apply {
                putExtra("conversationUser", user)
                putExtra("currentUser", currentUser)
                context!!.startActivity(this)
            }
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: RelativeLayout = view.find(R.id.rl_parent)
        val image: ImageView = view.find(R.id.iv_user_photo)
        val userName: TextView = view.find(R.id.tv_user_name)
        val age: TextView = view.find(R.id.tv_user_age)
        val city: TextView = view.find(R.id.tv_user_city)
    }
}