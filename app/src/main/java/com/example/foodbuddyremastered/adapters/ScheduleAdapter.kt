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
import com.example.foodbuddyremastered.models.Schedule
import com.example.foodbuddyremastered.models.User
import org.jetbrains.anko.find

class ScheduleAdapter(private var items: ArrayList<Schedule>,
                      private var context: Context?
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private val TAG = "ScheduleAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.schedule_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = items[position]

        holder.day.text = schedule.day
        if (schedule.is24Hours) {
            holder.time.text = "Open 24 hours"
        } else {
            holder.time.text = "${schedule.start} - ${schedule.end}"
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.find(R.id.tv_day)
        val time: TextView = view.find(R.id.tv_time)
    }
}