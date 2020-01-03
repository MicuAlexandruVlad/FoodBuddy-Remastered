package com.example.foodbuddyremastered.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.foodbuddyremastered.constants.Locations
import com.example.foodbuddyremastered.events.ErrorEvent
import com.example.foodbuddyremastered.utils.APIClient
import org.greenrobot.eventbus.EventBus

class LoginViewModel: ViewModel() {

    var email: String = ""
    var password: String = ""
    private var errorMessage: String = ""

    companion object {
        const val TAG = "LoginViewModel"
    }

    fun onLoginClicked() {
        if (!isInputDataValid()) {

            EventBus.getDefault().post(ErrorEvent().also {
                it.errorMessage = errorMessage
                it.location = Locations.LOGIN_VIEW_MODEL
            })
        } else {
            val client = APIClient()
            client.authUserEmail(email, password)
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
}