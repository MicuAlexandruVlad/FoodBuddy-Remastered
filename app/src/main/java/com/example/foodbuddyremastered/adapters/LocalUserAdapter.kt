package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import org.jetbrains.anko.find

class LocalUserAdapter(private var items: ArrayList<LocalUser>, private val owner: LifecycleOwner,
                       val itemClick: MutableLiveData<Int>)
    : RecyclerView.Adapter<LocalUserAdapter.ViewHolder>() {

    private val TAG = "LocalUserAdapter"
    private val liveData = MutableLiveData<ResponseEvent>()
    private val notifUtils = NotifUtils((owner as Context))

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.local_user_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localUser = items[position]

        holder.email.text = localUser.email
        holder.parent.setOnClickListener {
            APIClient().apply {
                authUserEmail(localUser.email, localUser.password, liveData)
            }

            itemClick.postValue(position)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: RelativeLayout = view.find(R.id.rl_parent)
        val email: TextView = view.find(R.id.tv_email)
    }
}