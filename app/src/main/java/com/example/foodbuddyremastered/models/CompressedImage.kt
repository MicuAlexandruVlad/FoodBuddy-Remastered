package com.example.foodbuddyremastered.models

import android.graphics.Bitmap
import java.io.Serializable
import java.lang.StringBuilder

class CompressedImage: Serializable {
    lateinit var bitmap: Bitmap
    lateinit var encodedValue: String
    lateinit var imageName: String
}