package com.example.foodbuddyremastered.models

import java.io.Serializable

class PlaceImage: Serializable {

    // unix
    lateinit var name: String
    var url = ""
    lateinit var signature: String

    // Bitmap I think
    lateinit var data: Any

    var location = -1
}