package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.databinding.ActivityAddPlaceBinding
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.AddPlaceViewModel


class AddPlaceActivity : AppCompatActivity() {

    private lateinit var viewModel: AddPlaceViewModel

    // TODO: add a open for 24 hour option in the schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        val currentUser = intent.getSerializableExtra("currentUser") as User

        val binding: ActivityAddPlaceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_place)
        viewModel = AddPlaceViewModel(application, this, this, currentUser)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.LAUNCH_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUri = data.data!!
                val imageStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(imageStream)

                viewModel.displayPhoto(bitmap, Locations.FROM_GALLERY)
            }
        }
    }
}
