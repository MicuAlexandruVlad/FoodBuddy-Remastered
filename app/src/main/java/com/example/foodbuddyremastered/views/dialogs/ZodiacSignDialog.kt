package com.example.foodbuddyremastered.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ZodiacSignAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.models.ZodiacSign
import org.jetbrains.anko.find

class ZodiacSignDialog(private val owner: LifecycleOwner, context: Context): Dialog(context) {

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
    var pickOne = false
    lateinit var selectedSign: ZodiacSign

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_zodiac_sign)

        recyclerView = find(R.id.rv_zodiac_signs)
        cancel = find(R.id.btn_cancel)
        done = find(R.id.btn_done)

        selectedSigns = ArrayList()
        initialList = ArrayList()
        initialList.addAll(ZodiacSign.getList())

        adapter = ZodiacSignAdapter(initialList, context)
        adapter.pickOne = pickOne
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.signs.observe(owner, Observer<ZodiacSign> { sign ->
            Log.d(TAG, "Observed change, action -> ${sign.action}")
            if (pickOne) {
                selectedSigns.clear()
                selectedSigns.add(sign)
            } else {
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
            }
        })

        cancel.setOnClickListener {
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