package com.example.foodbuddyremastered.models

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.foodbuddyremastered.R

class PlaceType {
    lateinit var name: String
    lateinit var drawable: Drawable

    var selected: Boolean = false

    companion object {
        fun getItems(context: Context): ArrayList<PlaceType> {
            return ArrayList<PlaceType>().apply {
                add(PlaceType().apply {
                    name = "Restaurant"
                    drawable = ContextCompat.getDrawable(context, R.drawable.restaurant)!!
                })

                add(PlaceType().apply {
                    name = "Fast food"
                    drawable = ContextCompat.getDrawable(context, R.drawable.fast_food)!!
                })

                add(PlaceType().apply {
                    name = "Cafe"
                    drawable = ContextCompat.getDrawable(context, R.drawable.cafe)!!
                })

                add(PlaceType().apply {
                    name = "Buffet"
                    drawable = ContextCompat.getDrawable(context, R.drawable.buffet)!!
                })
            }
        }
    }
}