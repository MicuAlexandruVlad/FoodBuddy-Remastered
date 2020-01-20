package com.example.foodbuddyremastered.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.utils.AnimationUtils
import org.jetbrains.anko.find

class PhotoOptionPickerDialog(context: Context): Dialog(context) {

    companion object {
        const val TAG = "PhotoOptionPickerDialog"
        const val ELEVATION = 12f
    }

    private lateinit var gallery: CardView
    private lateinit var camera: CardView
    private lateinit var cancel: Button
    private lateinit var done: Button

    var isDone = false
    var option = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_photo_option_picker)

        bindViews()

        gallery.setOnClickListener {
            if (option != Actions.GALLERY_PICKED) {
                if (option != -1) {
                    AnimationUtils.animateCardElevation(gallery, 0f, ELEVATION)
                    AnimationUtils.animateCardElevation(camera, ELEVATION, 0f)
                } else {
                    AnimationUtils.animateCardElevation(gallery, 0f, ELEVATION)
                }
            }

            option = Actions.GALLERY_PICKED
        }

        camera.setOnClickListener {
            if (option != Actions.CAMERA_PICKED) {
                if (option != -1) {
                    AnimationUtils.animateCardElevation(gallery, ELEVATION, 0f)
                    AnimationUtils.animateCardElevation(camera, 0f, ELEVATION)
                } else {
                    AnimationUtils.animateCardElevation(camera, 0f, ELEVATION)
                }
            }

            option = Actions.CAMERA_PICKED
        }

        cancel.setOnClickListener {
            dismiss()
        }

        done.setOnClickListener {
            isDone = true
            dismiss()
        }
    }

    private fun bindViews() {
        gallery = find(R.id.cv_gallery)
        camera = find(R.id.cv_camera)
        cancel = find(R.id.btn_cancel)
        done = find(R.id.btn_done)
    }
}