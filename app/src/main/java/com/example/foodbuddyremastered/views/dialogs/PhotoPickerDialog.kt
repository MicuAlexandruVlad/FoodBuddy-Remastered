package com.example.foodbuddyremastered.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import com.example.foodbuddyremastered.R
import org.jetbrains.anko.find

class PhotoPickerDialog(context: Context): Dialog(context) {

    private lateinit var done: Button
    private lateinit var cancel: Button
    private lateinit var rg: RadioGroup

    var isDone = false
    private var isChecked = false

    // 0 - camera, 1 - gallery, 2 - web
    var option = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_photo_picker)

        bindViews()

        rg.setOnCheckedChangeListener { p0, p1 ->
            isChecked = true
        }

        cancel.setOnClickListener {
            dismiss()
        }

        done.setOnClickListener {

            if (!isChecked) {
                Toast.makeText(context, "No option selected", Toast.LENGTH_SHORT).show()
            } else {
                val id = rg.checkedRadioButtonId
                option = rg.indexOfChild(rg.find(id))

                isDone = true
                dismiss()
            }


        }
    }

    private fun bindViews() {
        rg = find(R.id.rg)
        done = find(R.id.btn_done)
        cancel = find(R.id.btn_cancel)
    }
}