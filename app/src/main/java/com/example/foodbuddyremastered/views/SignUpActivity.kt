package com.example.foodbuddyremastered.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.databinding.ActivitySignUpBinding
import com.example.foodbuddyremastered.viewmodels.SignUpViewModel


class SignUpActivity : AppCompatActivity() {

    // TODO: switch this to MVVM

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
}
