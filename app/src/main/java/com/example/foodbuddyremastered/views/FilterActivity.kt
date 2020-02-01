package com.example.foodbuddyremastered.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.databinding.ActivityFilterBinding
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.FilterViewModel

class FilterActivity : AppCompatActivity() {

    companion object {
        const val TAG = "FilterActivity"
    }

    private lateinit var viewModel: FilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val binding: ActivityFilterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_filter)
        viewModel = FilterViewModel(application, this, this)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.currentUser = intent.getSerializableExtra("currentUser") as User

        viewModel.initView()
    }
}
