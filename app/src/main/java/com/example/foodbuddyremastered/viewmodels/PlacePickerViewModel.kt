package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.PlaceAdapter
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.JsonUtils
import com.example.foodbuddyremastered.views.AddPlaceActivity
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.json.JSONArray

class PlacePickerViewModel(app: Application,
                           private val context: Context,
                           private val owner: LifecycleOwner,
                           private val currentUser: User,
                           private val city: String,
                           private val country: String)
    : ObservableViewModel(app) {

    private var liveResponse = MutableLiveData<ResponseEvent>()
    private var places = ArrayList<Place>()
    private lateinit var adapter: PlaceAdapter


    companion object {
        const val TAG = "PlacePickerViewModel"
    }

    private lateinit var search: MaterialEditText
    private lateinit var barTitle: TextView
    private lateinit var placesRv: RecyclerView
    private lateinit var searchLl: LinearLayout
    private lateinit var noPlaces: LinearLayout




    fun initView() {
        bindViews()

        noPlaces.visibility = View.GONE
        placesRv.visibility = View.VISIBLE

        adapter = PlaceAdapter(places, context, currentUser)
        placesRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        placesRv.adapter = adapter

        APIClient().requestPlaces(city, country, 10, liveResponse)

        liveResponse.observe(owner, Observer<ResponseEvent> {
            when (it.status) {
                HttpStatus.SC_OK -> {
                    val array = it.payload as JSONArray

                    if (array.length() == 0) {
                        noPlaces.visibility = View.VISIBLE
                        placesRv.visibility = View.GONE
                    }

                    Log.d(TAG, "Response event payload -> ${Gson().toJson(array)}")

                    doAsync {
                        for (index in 0 until array.length()) {
                            places.add(JsonUtils.jsonObjectToPlace(array.getJSONObject(index)))
                            (context as Activity).runOnUiThread {
                                adapter.notifyItemInserted(places.size - 1)
                            }
                        }
                    }
                }
            }
        })

    }

    fun onSearch() {
        when (search.visibility) {
            View.VISIBLE -> {
                search.visibility = View.GONE
                barTitle.visibility = View.VISIBLE
            }

            View.GONE -> {
                search.visibility = View.VISIBLE
                barTitle.visibility = View.GONE
            }
        }
    }

    fun onPlaceCreated(place: Place) {
        places.add(place)
    }

    fun onAddPlace() {
        (context as Activity).startActivityForResult(Intent(context, AddPlaceActivity::class.java).apply {
            putExtra("currentUser", currentUser)
        }, RequestCodes.ADD_PLACE)
    }

    private fun bindViews() {
        with(context as Activity) {
            search = find(R.id.met_search_place)
            barTitle = find(R.id.tv_title)
            searchLl = find(R.id.ll_search)
            noPlaces = find(R.id.ll_no_places)
            placesRv = find(R.id.rv_places)

        }
    }


}