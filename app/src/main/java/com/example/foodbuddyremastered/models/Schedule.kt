package com.example.foodbuddyremastered.models

import java.io.Serializable

class Schedule: Serializable {

    lateinit var day: String
    lateinit var start: String
    lateinit var end: String

    var is24Hours = false
}