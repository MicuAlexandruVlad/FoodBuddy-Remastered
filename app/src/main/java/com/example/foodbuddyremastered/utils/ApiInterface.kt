package com.example.foodbuddyremastered.utils

import com.example.foodbuddyremastered.models.User

interface ApiInterface {

    fun registerUserEmail(user: User)

    fun authUserEmail(email: String, password: String)
}