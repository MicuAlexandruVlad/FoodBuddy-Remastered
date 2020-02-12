package com.example.foodbuddyremastered.models

import java.io.Serializable

class Review: Serializable {

    var id = ""
    lateinit var userName: String
    var placeId = ""
    lateinit var userId: String
    lateinit var content: String
    var rating: Double = 0.0
    lateinit var timestamp: String
}