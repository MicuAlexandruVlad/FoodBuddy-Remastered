package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.databinding.ActivityEventWizardBinding
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.EventWizardViewModel

class EventWizardActivity : AppCompatActivity() {

    private lateinit var viewModel: EventWizardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_wizard)

        val currentUser = intent.getSerializableExtra("currentUser") as User
        val conversationUser = intent.getSerializableExtra("conversationUser") as User

        val binding: ActivityEventWizardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_event_wizard)
        viewModel = EventWizardViewModel(application, this, this,
            currentUser, conversationUser)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = data.getSerializableExtra("place") as Place

                viewModel.onPlacePicked(place)
            }
        }
    }
}
