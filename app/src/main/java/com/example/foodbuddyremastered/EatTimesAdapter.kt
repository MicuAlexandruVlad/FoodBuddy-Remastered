package com.example.foodbuddyremastered

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.User
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.find

class EatTimesAdapter(private var eatTimes: ArrayList<EatTimes>,
                      private var context: Context,
                      private var currentUser: User
) : RecyclerView.Adapter<EatTimesAdapter.ViewHolder>() {

    companion object {
        const val TAG = "EatTimesAdapter"
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.eat_times_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eatTimes.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eatTime = EatTimes()


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val start: MaterialEditText = view.find(R.id.met_start)
        val end: MaterialEditText = view.find(R.id.met_end)
    }
}