package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.events.ErrorEvent
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import org.greenrobot.eventbus.EventBus

class LoginViewModel(val context: Context): ViewModel() {

    var email: String = ""
    var password: String = ""
    private var errorMessage: String = ""
    var remember = false
    var response = MutableLiveData<ResponseEvent>()
    var notifUtils = NotifUtils(context)

    companion object {
        const val TAG = "LoginViewModel"
    }

    fun onLoginClicked() {
        if (!isInputDataValid()) {
            notifUtils.createToast(errorMessage).show()
        } else {
            writeToSharedPreferences(remember)
            val client = APIClient()
            client.authUserEmail(email, password, response)
        }
    }

    private fun isInputDataValid(): Boolean {

        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "One or more fields are empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Email address is not valid"
            return false
        }

        return true
    }

    fun onRemember(b: Boolean) {
        remember = b
    }

    private fun writeToSharedPreferences(b: Boolean) {
        val pref = context.getSharedPreferences("SP", Context.MODE_PRIVATE)

        with(pref.edit()) {
            putBoolean(context.getString(R.string.remember), b)
            commit()
        }
    }
}