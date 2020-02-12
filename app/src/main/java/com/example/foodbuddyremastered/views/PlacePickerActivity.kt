package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.databinding.ActivityPlacePickerBinding
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.PlacePickerViewModel

class PlacePickerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlacePickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_picker)

        val currentUser = intent.getSerializableExtra("currentUser") as User
        val city = intent.getStringExtra("city") as String
        val country = intent.getStringExtra("country") as String

        val binding: ActivityPlacePickerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_place_picker)
        viewModel = PlacePickerViewModel(application, this, this,
            currentUser, city, country)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.ADD_PLACE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = data.getSerializableExtra("place") as Place
                viewModel.onPlaceCreated(place)

            }
        }

        if (requestCode == RequestCodes.PLACE_DETAILS) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = data.getSerializableExtra("place") as Place

                intent.putExtra("place", place)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
