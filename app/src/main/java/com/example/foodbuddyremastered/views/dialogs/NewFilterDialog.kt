package com.example.foodbuddyremastered.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.utils.AnimationUtils
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.find

class NewFilterDialog(context: Context): Dialog(context) {

    companion object {
        const val TAG = "NewFilterDialog"
    }

    private lateinit var filterName: MaterialEditText
    private lateinit var cancel: Button
    private lateinit var done: Button
    lateinit var name: String

    var isDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_new_filter)

        bindViews()


        cancel.setOnClickListener {
            dismiss()
        }

        done.setOnClickListener {
            if (filterName.text.isNullOrBlank()) {
                Toast.makeText(context, "Filter name is empty", Toast.LENGTH_SHORT).show()
            } else {
                isDone = true
                name = filterName.text.toString()
                dismiss()
            }
        }
    }

    private fun bindViews() {
        filterName = find(R.id.met_filter_name)
        cancel = find(R.id.btn_cancel)
        done = find(R.id.btn_done)
    }
}