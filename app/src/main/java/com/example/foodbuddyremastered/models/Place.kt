package com.example.foodbuddyremastered.models

import java.io.Serializable

class Place: Serializable {

    lateinit var id: String
    lateinit var photoId: ArrayList<String>
    lateinit var name: String
    lateinit var address: Address
    var description = ""
    var rating: Double = 0.0
    var numReviews = 0
    var visitors = 0
    // Local variable
    lateinit var reviews: ArrayList<Review>
    lateinit var placeType: String
    var hasSchedule = false
    lateinit var schedule: HashMap<String, String>

    lateinit var createdBy: String
    lateinit var lastEditedBy: String
}