package com.example.foodbuddyremastered.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

class User: Serializable {

    var id: String = ""
    var photoId: String = ""
    var email: String = ""
    var password: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var city: String = ""
    var country: String = ""
    var phoneNumber: String = ""
    var gender: String = ""
    var age: Int = 0
    var zodiacSign: String = ""
    val eatTimes: ArrayList<EatTimes> = ArrayList()
    var partnerGender: String = ""
    var partnerMinAge: Int = 0
    var partnerMaxAge: Int = 0

    var hasPhoto: Boolean = false
    var profileComplete: Boolean = false

    lateinit var compressedImage: CompressedImage
}