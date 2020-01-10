package com.example.foodbuddyremastered.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.foodbuddyremastered.R
import java.io.Serializable

class ZodiacSign : Serializable {

    var name: String = ""
    var selected = false

    // 3 = remove, 2 = add
    var action = 3

    companion object {

        fun getList(context: Context): ArrayList<ZodiacSign> {
            return ArrayList<ZodiacSign>().apply {
                add(ZodiacSign().also {
                    it.name = "Aquarius"
                })

                add(ZodiacSign().also {
                    it.name = "Aries"
                })

                add(ZodiacSign().also {
                    it.name = "Cancer"
                })

                add(ZodiacSign().also {
                    it.name = "Capricorn"
                })

                add(ZodiacSign().also {
                    it.name = "Gemini"
                })

                add(ZodiacSign().also {
                    it.name = "Leo"
                })

                add(ZodiacSign().also {
                    it.name = "Libra"
                })

                add(ZodiacSign().also {
                    it.name = "Pisces"
                })

                add(ZodiacSign().also {
                    it.name = "Sagittarius"
                })

                add(ZodiacSign().also {
                    it.name = "Scorpio"
                })

                add(ZodiacSign().also {
                    it.name = "Taurus"
                })

                add(ZodiacSign().also {
                    it.name = "Virgo"
                })
            }
        }
    }
}