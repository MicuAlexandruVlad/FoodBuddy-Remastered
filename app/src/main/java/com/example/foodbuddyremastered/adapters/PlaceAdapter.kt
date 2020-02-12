package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.views.PlaceActivity
import org.jetbrains.anko.find

class PlaceAdapter(private var items: ArrayList<Place>,
                   private var context: Context?,
                   private var currentUser: User
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private val TAG = "PlaceAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.place_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]

        holder.parent.setOnClickListener {
            (context as Activity).startActivityForResult(Intent(context, PlaceActivity::class.java).apply {
                putExtra("currentUser", currentUser)
                putExtra("position", position)
                putExtra("place", place)
            }, RequestCodes.PLACE_DETAILS)
        }

        holder.openMap.setOnClickListener {

        }

        Glide.with(context!!).load(ApiUrls.getPlacePhoto(place.id, place.photoId[0])).into(holder.image)
        holder.address.text = "${place.address.street}, ${place.address.city}, ${place.address.postalCode}"
        holder.placeName.text = place.name
        holder.rating.rating = place.rating.toFloat()
        holder.numReviews.text = "(${place.numReviews})"
        holder.visitors.text = "${place.visitors} people have been here"
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: LinearLayout = view.find(R.id.ll_parent)
        val image: ImageView = view.find(R.id.iv_place_image)
        val placeName: TextView = view.find(R.id.tv_place_name)
        val rating: RatingBar = view.find(R.id.rb_place_rating)
        val numReviews: TextView = view.find(R.id.tv_num_reviews)
        val address: TextView = view.find(R.id.tv_place_address)
        val openMap: RelativeLayout = view.find(R.id.rl_open_map)
        val visitors: TextView = view.find(R.id.tv_num_visitors)
    }
}