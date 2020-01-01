package com.example.foodbuddyremastered.utils

import android.content.Context
import android.widget.Toast

class NotifUtils constructor(val context: Context) {

    fun createToast(message: String): Toast {
        return Toast.makeText(context, message, Toast.LENGTH_SHORT)
    }
}