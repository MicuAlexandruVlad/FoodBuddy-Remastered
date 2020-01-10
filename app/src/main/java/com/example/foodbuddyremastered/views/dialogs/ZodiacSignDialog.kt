package com.example.foodbuddyremastered.views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.DialogCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ZodiacSignAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.models.ZodiacSign
import com.example.foodbuddyremastered.views.FilterActivity
import com.google.gson.Gson
import org.jetbrains.anko.find
import kotlin.math.sign

class ZodiacSignDialog(private val activity: FilterActivity): Dialog(activity) {

    companion object {
        const val TAG = "ZodiacSignDialog"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var cancel: Button
    private lateinit var done: Button

    private lateinit var adapter: ZodiacSignAdapter
    private lateinit var selectedSigns: ArrayList<ZodiacSign>
    private lateinit var initialList: ArrayList<ZodiacSign>
    var isDone = false
    var isCancel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_zodiac_sign)

        recyclerView = find(R.id.rv_zodiac_signs)
        cancel = find(R.id.btn_cancel)
        done = find(R.id.btn_done)

        selectedSigns = ArrayList()
        initialList = ArrayList()
        initialList.addAll(ZodiacSign.getList(context))

        adapter = ZodiacSignAdapter(initialList, context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.signs.observe(activity, Observer<ZodiacSign> { sign ->
            Log.d(TAG, "Observed change, action -> ${sign.action}")
            when (sign.action) {
                Actions.ADD_ZODIAC_SIGN -> {
                    selectedSigns.add(sign)
                    Log.d(TAG, "Signs -> ${selectedSigns.size}")
                }

                Actions.REMOVE_ZODIAC_SIGN -> {
                    removeSign(sign.name)
                    Log.d(TAG, "Signs -> ${selectedSigns.size}")
                }
            }
        })

        cancel.setOnClickListener {
            isCancel = true
            dismiss()
        }

        done.setOnClickListener {
            isDone = true
            dismiss()
        }
    }

    private fun removeSign(name: String) {
        for (index in selectedSigns.indices) {
            if (selectedSigns[index].name.compareTo(name) == 0) {
                selectedSigns.removeAt(index)
                break
            }
        }
    }

    fun getSelectedSigns(): List<ZodiacSign> {
        return selectedSigns
    }

    fun setSelectedSigns(signs: ArrayList<ZodiacSign>) {
        for (index in signs.indices) {
            val sign = signs[index]
            selectedSigns.add(sign)

            for (i in initialList.indices) {
                if (initialList[i].name.compareTo(sign.name) == 0) {
                    initialList[i].selected = true
                    adapter.notifyItemChanged(i)
                }
            }

            Log.d(TAG,"Added item -> ${sign.name}, Action: ${sign.action}, Selected: ${sign.selected}")
        }
    }
}