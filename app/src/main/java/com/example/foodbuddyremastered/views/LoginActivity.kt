package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.databinding.ActivityLoginBinding
import com.example.foodbuddyremastered.events.ErrorEvent
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.viewmodels.LoginViewModel
import com.example.foodbuddyremastered.views.dialogs.SelectUserDialog
import com.rengwuxian.materialedittext.MaterialEditText
import cz.msebera.android.httpclient.HttpStatus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private lateinit var notifUtils: NotifUtils
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        notifUtils = NotifUtils(this)

        viewModel = LoginViewModel(this)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        // observe server's response
        viewModel.response.observe(this, Observer<ResponseEvent> { event ->
            if (event.action == Actions.AUTH_USER_EMAIL_RESPONSE) {
                when (event.status) {
                    HttpStatus.SC_OK -> {
                        val user = event.payload as User
                        if (viewModel.remember) {
                            // save user in db if remember me is checked
                            Repository(this).apply {
                                doAsync {
                                    if (findUser(user.email).isEmpty()) {
                                        insertUser(LocalUser().apply {
                                            email = viewModel.email
                                            password = viewModel.password
                                            Log.d(TAG, "User with email $email has been inserted in RoomDB")
                                        })
                                    }
                                }
                            }
                        }
                        startActivity(Intent(this, MainActivity::class.java).apply {
                            putExtra("currentUser", user)
                        })
                    }

                    else -> {
                        notifUtils.createToast("User not found").show()
                    }
                }
            }
        })
    }


}
