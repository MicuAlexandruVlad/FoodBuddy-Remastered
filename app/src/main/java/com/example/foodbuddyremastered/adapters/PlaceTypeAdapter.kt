package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.models.PlaceType
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.find

class PlaceTypeAdapter(private var items: ArrayList<PlaceType>,
                       private var context: Context?
) : RecyclerView.Adapter<PlaceTypeAdapter.ViewHolder>() {

    private val TAG = "PlaceTypeAdapter"
    var typeSelected = false
    lateinit var selectedPlaceType: PlaceType

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.place_type_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeType = items[position]

        Glide.with(context!!).load(placeType.drawable).into(holder.icon)
        holder.placeName.text = placeType.name

        setDrawable(placeType, holder)

        holder.parent.setOnClickListener {
            typeSelected = true
            placeType.selected = true
            selectedPlaceType = placeType
            setDrawable(placeType, holder)
            resetDataSet(position)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: RelativeLayout = view.find(R.id.parent)
        val icon: ImageView = view.find(R.id.iv_place_type_icon)
        val placeName: TextView = view.find(R.id.tv_place_type_name)
    }

    private fun setDrawable(placeType: PlaceType, holder: ViewHolder) {
        if (placeType.selected) {
            when {
                placeType.name.compareTo("Restaurant") == 0 -> {
                    holder.parent.backgroundDrawable =
                        ContextCompat.getDrawable(context!!, R.drawable.restaurant_round)
                }

                placeType.name.compareTo("Fast food") == 0 -> {
                    holder.parent.backgroundDrawable =
                        ContextCompat.getDrawable(context!!, R.drawable.fast_food_round)
                }

                placeType.name.compareTo("Cafe") == 0 -> {
                    holder.parent.backgroundDrawable =
                        ContextCompat.getDrawable(context!!, R.drawable.cafe_round)
                }

                else -> {
                    holder.parent.backgroundDrawable =
                        ContextCompat.getDrawable(context!!, R.drawable.buffet_round)
                }
            }
        } else {
            holder.parent.backgroundDrawable =
                ContextCompat.getDrawable(context!!, R.drawable.grey_round)
        }
    }

    private fun resetDataSet(position: Int) {
        for (index in items.indices) {
            if (index != position && items[index].selected) {
                items[index].selected = false
                notifyItemChanged(index)
            }
        }
    }
}