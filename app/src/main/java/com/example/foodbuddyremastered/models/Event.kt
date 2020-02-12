package com.example.foodbuddyremastered.models

import androidx.room.PrimaryKey
import java.io.Serializable

class Event: Serializable {

    //@PrimaryKey(autoGenerate = true)
    //var id: Int? = null

    lateinit var name: String
    lateinit var date: String
    lateinit var place: Place

    lateinit var userId: String
    lateinit var partnerId: String
}