package com.example.foodbuddyremastered.utils

import androidx.lifecycle.MutableLiveData
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter

interface ApiInterface {

    fun registerUserEmail(user: User)

    fun authUserEmail(email: String, password: String)

    fun discoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>, user: User)
}