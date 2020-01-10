package com.example.foodbuddyremastered.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.TextMessage
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.ZodiacSign
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.imageBitmap

class ZodiacSignAdapter(private var items: ArrayList<ZodiacSign>,
                        private var context: Context?
) : RecyclerView.Adapter<ZodiacSignAdapter.ViewHolder>() {

    private val TAG = "ZodiacSignAdapter"
    val signs = MutableLiveData<ZodiacSign>()
    var displaySelected = true

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.sign_list_item, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val zodiacSign = items[position]

        holder.image.setImageDrawable(getDrawable(zodiacSign.name))
        holder.signName.text = zodiacSign.name

        if (displaySelected) {
            if (zodiacSign.selected) {
                holder.selected.visibility = View.VISIBLE
            }
        }

        holder.parent.setOnClickListener {
            Log.d(TAG, "Clicked on item at $position")

            if (!zodiacSign.selected) {
                zodiacSign.selected = true
                zodiacSign.action = Actions.ADD_ZODIAC_SIGN
                if (displaySelected) {
                    holder.selected.visibility = View.VISIBLE
                }
            } else {
                zodiacSign.selected = false
                zodiacSign.action = Actions.REMOVE_ZODIAC_SIGN
                if (displaySelected) {
                    holder.selected.visibility = View.GONE
                }
            }

            signs.postValue(zodiacSign)
        }
    }

    private fun getDrawable(name: String): Drawable {
        return when {
            name.compareTo("Aquarius") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.aquarius) as Drawable

            name.compareTo("Aries") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.aries) as Drawable

            name.compareTo("Cancer") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.cancer) as Drawable

            name.compareTo("Capricorn") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.capricorn) as Drawable

            name.compareTo("Gemini") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.gemini) as Drawable

            name.compareTo("Leo") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.leo) as Drawable

            name.compareTo("Libra") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.libra) as Drawable

            name.compareTo("Pisces") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.pisces) as Drawable

            name.compareTo("Sagittarius") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.sagittarius) as Drawable

            name.compareTo("Scorpio") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.scorpio) as Drawable

            name.compareTo("Taurus") == 0 -> ContextCompat
                .getDrawable(context!!, R.drawable.taurus) as Drawable

            else -> ContextCompat
                .getDrawable(context!!, R.drawable.virgo) as Drawable
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val parent: RelativeLayout = view.find(R.id.rl_parent)
        val image: ImageView = view.find(R.id.iv_sign)
        val selected: ImageView = view.find(R.id.iv_selected)
        val signName: TextView = view.find(R.id.tv_sign_name)
    }
}