package com.example.foodbuddyremastered.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import android.util.Log
import com.example.foodbuddyremastered.models.CompressedImage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.sign


class ImageUtils {

    companion object {
        const val SIZE_4K = 3686400
        const val SIZE_2K = 2800000

        const val TAG = "ImageUtils"

        fun resizeImage(bitmap: Bitmap, size: Int): Bitmap {

            var bmp = Bitmap.createBitmap(bitmap)

            Log.d(TAG, "Original width -> ${bitmap.width}")
            Log.d(TAG, "Original height -> ${bitmap.height}")

            var step = 0

            while (bmp.width * bmp.height >= size) {
                bmp = Bitmap.createScaledBitmap(bmp, (bmp.width / 1.2).toInt(),
                    (bmp.height / 1.2).toInt(), false)

                step++
                Log.d(TAG, "Step $step")
                Log.d(TAG, "Width -> ${bmp.width}")
                Log.d(TAG, "Height -> ${bmp.height}")
            }

            return bitmap
        }

        fun compressImage(bitmap: Bitmap, quality: Int, location: Int = -1): CompressedImage {
            return CompressedImage().apply {
                val bao = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao)
                this.bitmap = bitmap
                val bytes = bao.toByteArray()
                this.encodedValue = Base64.encodeToString(bytes, Base64.DEFAULT)
                val time = getUnix()
                imageName = time
                signature = time
                this.location = location
            }

        }

        fun compressImageNew(bitmap: Bitmap, quality: Int, location: Int = -1): CompressedImage {
            return CompressedImage().apply {
                val bao = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao)
                val bytes = bao.toByteArray()
                this.encodedValue = Base64.encodeToString(bytes, Base64.DEFAULT)
                val time = getUnix()
                imageName = time
                signature = time
                this.location = location
            }
        }

        private fun getUnix(): String {
            return Calendar.getInstance().time.time.toString()
        }
    }
}