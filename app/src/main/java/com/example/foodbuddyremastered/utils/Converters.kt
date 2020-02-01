package com.example.foodbuddyremastered.utils

import androidx.room.TypeConverter
import com.example.foodbuddyremastered.models.ZodiacSign
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONArray
import java.lang.StringBuilder

class Converters {

    @TypeConverter
    fun fromZodiacArray(value: ArrayList<ZodiacSign>?): String {
        val builder = StringBuilder()

        // pattern -> name!selected!action_name!selected!action
        builder.apply {
            for (index in value!!.indices) {
                val sign = value[index]
                builder.append(sign.name)
                builder.append("!")
                builder.append(sign.selected)
                builder.append("!")
                builder.append(sign.action)
                if (index < value.size - 1) {
                    builder.append("_")
                }
            }
        }

        return builder.toString()
    }

    @TypeConverter
    fun stringToZodiacArray(value: String?): ArrayList<ZodiacSign> {
        return ArrayList<ZodiacSign>().apply {
            val obj = value!!.split("_")
            for (i in obj) {
                add(ZodiacSign().apply {
                    name = i.split("!")[0]
                    selected = i.split("!")[1].toBoolean()
                    action = i.split("!")[2].toInt()
                })
            }
        }
    }
}