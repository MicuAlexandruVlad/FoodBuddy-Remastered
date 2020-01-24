package com.example.foodbuddyremastered.utils.database

import java.util.*

class TimeUtils {
    companion object {
        fun getCurrentTime(): String {
            return Calendar.getInstance().time.toString()
        }

        fun getCurrentHour(): String {
            return getCurrentTime().split(" ")[3].split(":")[0] +
                    ":" + getCurrentTime().split(" ")[3].split(":")[1]
        }
    }
}