package com.example.foodbuddyremastered.models

import java.io.Serializable

class UserFilter(var start: String = "", var end: String = ""): Serializable {

    lateinit var city: String
    lateinit var country: String
    var minAge: Int = 18
    var maxAge: Int = 18
    lateinit var gender: String
    var tolerance: Int = 0
    var zodiacSigns = ArrayList<ZodiacSign>()

}