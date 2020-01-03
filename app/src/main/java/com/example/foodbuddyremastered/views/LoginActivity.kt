package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.databinding.ActivityLoginBinding
import com.example.foodbuddyremastered.events.ErrorEvent
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.viewmodels.LoginViewModel
import com.rengwuxian.materialedittext.MaterialEditText
import cz.msebera.android.httpclient.HttpStatus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find

class LoginActivity : AppCompatActivity() {

    private lateinit var notifUtils: NotifUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        EventBus.getDefault().register(this)
        notifUtils = NotifUtils(this)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = LoginViewModel()
        binding.executePendingBindings()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(errorEvent: ErrorEvent) {
        when (errorEvent.location) {
            Locations.LOGIN_VIEW_MODEL -> {
                notifUtils.createToast(errorEvent.errorMessage).show()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResponseEvent(responseEvent: ResponseEvent) {
        when (responseEvent.status) {
            HttpStatus.SC_OK -> {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra("currentUser", responseEvent.payload as User)
                })
            }

            else -> {
                notifUtils.createToast("User not found").show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
