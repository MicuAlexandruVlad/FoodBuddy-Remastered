package com.example.foodbuddyremastered.utils

import android.animation.ValueAnimator
import androidx.cardview.widget.CardView

class AnimationUtils {
    companion object {
        fun animateCardElevation(view: CardView, start: Float, end: Float) {
            val animator = ValueAnimator.ofFloat(start, end)
            val duration = 800
            animator.addUpdateListener {
                view.cardElevation = it.animatedValue as Float
            }

            animator.start()
        }
    }
}