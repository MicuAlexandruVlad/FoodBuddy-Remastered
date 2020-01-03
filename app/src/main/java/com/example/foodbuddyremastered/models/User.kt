package com.example.foodbuddyremastered.models

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
    val eatTimes: ArrayList<EatTimes> = ArrayList<EatTimes>()
    var partnerGender: String = ""
    var partnerMinAge: Int = 0
    var partnerMaxAge: Int = 0

    var hasPhoto: Boolean = false
    var profileComplete: Boolean = false

}