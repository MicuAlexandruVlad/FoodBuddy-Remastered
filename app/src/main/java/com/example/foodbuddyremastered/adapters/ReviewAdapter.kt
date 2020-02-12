package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.Review
import com.example.foodbuddyremastered.models.User
import org.jetbrains.anko.find

class ReviewAdapter(private var items: ArrayList<Review>,
                    private var context: Context) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private val TAG = "DiscoverAdapter"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.review_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = items[position]

        //Glide.with(context!!).load(ApiUrls.getUserPhotoSmall(review.userId, "")).into(holder.userPhoto)
        holder.userPhoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder))
        holder.timestamp.text = review.timestamp
        holder.rating.rating = review.rating.toFloat()
        holder.userName.text = review.userName
        holder.reviewText.text = review.content
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.find(R.id.tv_user_name)
        val timestamp: TextView = view.find(R.id.tv_timestamp)
        val rating: RatingBar = view.find(R.id.rb_review_rating)
        val reviewText: TextView = view.find(R.id.tv_review_text)
        val userPhoto: ImageView = view.find(R.id.iv_user_photo)
    }
}