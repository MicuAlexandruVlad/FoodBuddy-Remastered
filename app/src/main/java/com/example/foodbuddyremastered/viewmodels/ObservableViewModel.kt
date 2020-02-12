package com.example.foodbuddyremastered.viewmodels

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel

abstract class ObservableViewModel(app: Application): AndroidViewModel(app), Observable {
    var callBacks = PropertyChangeRegistry()

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    fun notifyChange() {
        callBacks.notifyChange(this, 0)
    }

    fun notifyChange(viewId:Int){
        callBacks.notifyChange(this, viewId)
    }

}