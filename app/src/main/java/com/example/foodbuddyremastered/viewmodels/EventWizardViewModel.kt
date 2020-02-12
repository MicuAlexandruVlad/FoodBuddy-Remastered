package com.example.foodbuddyremastered.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.view.SurfaceHolder
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.views.PlacePickerActivity
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.find
import java.util.*


class EventWizardViewModel(app: Application,
                           private val context: Context,
                           private val owner: LifecycleOwner,
                           private val currentUser: User,
                           private val conversationUser: User)
    : ObservableViewModel(app) {

    private lateinit var name: MaterialEditText
    private lateinit var city: MaterialEditText
    private lateinit var country: MaterialEditText
    private lateinit var time: TextView
    private lateinit var timeLabel: TextView
    private lateinit var date: TextView
    private lateinit var dateLabel: TextView
    private lateinit var locationLabel: TextView
    private lateinit var locationPlaceHolder: TextView
    private lateinit var placeHolder: LinearLayout
    private lateinit var placeName: TextView
    private lateinit var placeAddress: TextView
    private lateinit var placeImage: ImageView

    private var notifUtils = NotifUtils(context)

    private var minute = 0
    private var hour = 0
    private var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private var month = Calendar.getInstance().get(Calendar.MONTH)

    fun initView() {

        bindViews()

        placeHolder.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun onTimeChange() {
        val timePicker = TimePickerDialog(context,
            OnTimeSetListener { _, selectedHour, selectedMinute ->
                timeLabel.visibility = View.GONE
                time.visibility = View.VISIBLE
                minute = selectedMinute
                hour = selectedHour
                time.text = "$selectedHour:$selectedMinute"
            },
            hour,
            minute,
            true
        ) //Yes 24 hour time

        timePicker.setTitle("Select Time")
        timePicker.show()
    }

    @SuppressLint("SetTextI18n")
    fun onDateChange() {
        val picker = DatePickerDialog(
            context,
            OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                date.text = selectedDay.toString() + "/" + (selectedMonth + 1) + "/" + selectedYear
                date.visibility = View.VISIBLE
                dateLabel.visibility = View.GONE
                year = selectedYear
                month = selectedMonth
                day = selectedDay
            },
            year,
            month,
            day
        )
        picker.show()
    }

    fun onPlaceChange() {
        if (city.text.isNullOrEmpty() || country.text.isNullOrEmpty()) {
            notifUtils.createToast("City and country fields can not be empty").show()
        } else {
            (context as Activity).startActivityForResult(Intent(context, PlacePickerActivity::class.java).apply {
                putExtra("currentUser", currentUser)
                putExtra("city", city.text.toString())
                putExtra("country", country.text.toString())
            }, RequestCodes.PLACE_PICKER)
        }
    }

    @SuppressLint("SetTextI18n")
    fun onPlacePicked(place: Place) {
        locationPlaceHolder.visibility = View.GONE
        placeHolder.visibility = View.VISIBLE

        Glide.with(context).load(ApiUrls.getPlacePhoto(place.id, place.photoId[0])).into(placeImage)
        placeName.text = place.name
        placeAddress.text = "${place.address.street} ${place.address.postalCode}"
    }

    private fun bindViews() {
        context as Activity
        with(context) {
            name = find(R.id.met_event_name)
            city = find(R.id.met_event_city)
            country = find(R.id.met_event_country)
            timeLabel = find(R.id.tv_event_time_label)
            time = find(R.id.tv_event_time)
            date = find(R.id.tv_event_date)
            dateLabel = find(R.id.tv_event_date_label)
            locationPlaceHolder = find(R.id.tv_event_place)
            locationLabel = find(R.id.tv_location_label)
            placeHolder = find(R.id.ll_place_holder)
            placeName = find(R.id.tv_place_name)
            placeAddress = find(R.id.tv_place_address)
            placeImage = find(R.id.iv_place_image)
        }
    }
}