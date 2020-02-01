package com.example.foodbuddyremastered.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.foodbuddyremastered.models.*
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.database.Repository
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.SyncHttpClient
import org.jetbrains.anko.doAsync

class MainActivityViewModel : ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
    }

    private val api = APIClient()
    lateinit var currentUser: User
    lateinit var filter: UserFilter
    lateinit var context: Context
    lateinit var lifecycleOwner: LifecycleOwner
    var liveActiveFilter = MutableLiveData<UserFilter>()
    var filteredUsers = MutableLiveData<List<User>>()

    fun discoverUsers() {
        // get the last active filter, if there is none => no filter is in DB => use the custom filter and store it in DB

        Repository(context).getActiveFilterLive(currentUser.id, true).observe(lifecycleOwner,
            Observer<List<UserFilter>> {
                if (it.size == 1 && it.isNotEmpty()) {
                    Log.d(TAG, "Found an active filter")
                    Log.d(TAG, "Active filter data -> ${Gson().toJson(it)}")
                    this@MainActivityViewModel.filter = it[0]
                } else {
                    doAsync {
                        Log.d(TAG, "Inserting default filter")
                        insertDefaultFilter()
                    }

                    this@MainActivityViewModel.filter = computeDefaultFilter()
                }

                apiDiscoverUsers(filter, filteredUsers)
            })

        Log.d(TAG, "Default filter ${Gson().toJson(computeDefaultFilter().zodiacSigns)}")
    }

    private fun apiDiscoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>, client: AsyncHttpClient = AsyncHttpClient()) {
        api.discoverUsers(filter, list, currentUser, client)
    }

    private fun computeDefaultFilter(): UserFilter {
        return UserFilter().also { filter ->
            filter.start = currentUser.eatTimes[0].start
            filter.end = currentUser.eatTimes[0].end
            filter.maxAge = currentUser.partnerMaxAge
            filter.minAge = currentUser.partnerMinAge
            filter.gender = currentUser.partnerGender
            filter.city = currentUser.city
            filter.country = currentUser.country
            filter.zodiacSigns.addAll(ZodiacSign.getList())
            filter.type = UserFilter.DEFAULT_FILTER
            filter.isLastUsed = true
            filter.ownerId = currentUser.id
            filter.name = "Default Filter"
        }
    }

    private fun getActiveFilter(): List<UserFilter> {
        return Repository(context).getActiveFilter(currentUser.id, true)
    }

    private fun insertDefaultFilter() {
        Repository(context).insertFilter(computeDefaultFilter())
    }
}