package com.example.foodbuddyremastered.models

import java.io.Serializable

class Address: Serializable {

    lateinit var street: String
    lateinit var city: String
    lateinit var country: String
    lateinit var postalCode: String
}