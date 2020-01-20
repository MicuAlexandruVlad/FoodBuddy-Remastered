package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.databinding.ActivitySignUpBinding
import com.example.foodbuddyremastered.viewmodels.SignUpViewModel


class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var viewModel: SignUpViewModel



    // TODO: add profile picture feature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val binding: ActivitySignUpBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = SignUpViewModel(application, this)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.LAUNCH_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val imageUri = data.data!!
                val imageStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(imageStream)

                viewModel.displayPhoto(bitmap)
            }
        }

        if (requestCode == RequestCodes.LAUNCH_CAMERA && resultCode == Activity.RESULT_OK) {
            // TODO: get the photo from the camera and display it
        }
    }
}
