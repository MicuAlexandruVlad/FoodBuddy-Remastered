package com.example.foodbuddyremastered.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

class PlaceViewModel: ViewModel() {
    lateinit var context: Context
    lateinit var owner: LifecycleOwner
}