package com.example.foodbuddyremastered.utils

import android.content.Context


class DisplayUtils {

    companion object {
        fun calculateNumberOfColumns(context: Context, columnWidth: Float): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density

            return (screenWidthDp / columnWidth + 0.5).toInt()
        }
    }
}