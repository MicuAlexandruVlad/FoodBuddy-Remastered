package com.example.foodbuddyremastered.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.example.foodbuddyremastered.utils.APIClient

class MainActivityViewModel : ViewModel() {

    private val client = APIClient()
    lateinit var currentUser: User
    lateinit var filter: UserFilter
    var filteredUsers = MutableLiveData<List<User>>()

    fun discoverUsers(filter: UserFilter = computeDefaultFilter()) {
        this.filter = filter
        
        discoverUsers(filter, filteredUsers)
    }

    private fun discoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>) {
        client.discoverUsers(filter, list, currentUser)
    }

    private fun computeDefaultFilter(): UserFilter {
        return UserFilter(currentUser.eatTimes[0].start, currentUser.eatTimes[0].end).also { filter ->
            filter.maxAge = currentUser.partnerMaxAge
            filter.minAge = currentUser.partnerMinAge
            filter.gender = currentUser.partnerGender
            filter.city = currentUser.city
            filter.country = currentUser.country
        }
    }

}