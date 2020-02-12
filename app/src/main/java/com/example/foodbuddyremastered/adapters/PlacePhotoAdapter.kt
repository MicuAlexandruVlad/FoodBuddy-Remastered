package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.models.CompressedImage
import com.example.foodbuddyremastered.models.PlaceImage
import com.example.foodbuddyremastered.models.User
import org.jetbrains.anko.find

class PlacePhotoAdapter(private var items: ArrayList<PlaceImage>,
                        private var context: Context?,
                        private var currentUser: User
) : RecyclerView.Adapter<PlacePhotoAdapter.ViewHolder>() {

    private val TAG = "DiscoverAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.place_image_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeImage = items[position]

        if (placeImage.location == Locations.FROM_WEB) {
            Glide.with(context!!).load(placeImage.url).into(holder.image)
        } else {
            Glide.with(context!!).load(placeImage.data as Bitmap).into(holder.image)
        }

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: LinearLayout = view.find(R.id.ll_parent)
        val image: ImageView = view.find(R.id.iv_place_image)
    }
}