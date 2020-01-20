package com.example.foodbuddyremastered.views

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ZodiacSignAdapter
import com.example.foodbuddyremastered.models.UserFilter
import com.example.foodbuddyremastered.models.ZodiacSign
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.views.dialogs.ZodiacSignDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rengwuxian.materialedittext.MaterialEditText
import com.xw.repo.BubbleSeekBar
import org.jetbrains.anko.find

class FilterActivity : AppCompatActivity() {

    companion object {
        const val TAG = "FilterActivity"
    }

    private lateinit var filter: UserFilter
    private lateinit var notifUtils: NotifUtils

    private lateinit var parent: NestedScrollView
    private lateinit var city: MaterialEditText
    private lateinit var country: MaterialEditText
    private lateinit var minAge: BubbleSeekBar
    private lateinit var maxAge: BubbleSeekBar
    private lateinit var addSigns: ImageView
    private lateinit var selectedSignsRV: RecyclerView
    private lateinit var tolerance: BubbleSeekBar
    private lateinit var toleranceInfo: ImageView
    private lateinit var male: RelativeLayout
    private lateinit var maleOn: ImageView
    private lateinit var female: RelativeLayout
    private lateinit var femaleOn: ImageView
    private lateinit var start: MaterialEditText
    private lateinit var end: MaterialEditText
    private lateinit var cancel: FloatingActionButton
    private lateinit var done: FloatingActionButton

    private lateinit var zodiacSignAdapter: ZodiacSignAdapter
    private lateinit var selectedSigns: ArrayList<ZodiacSign>

    private var isMale = false
    private var isFemale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        notifUtils = NotifUtils(this)
        filter = intent.getSerializableExtra("filter") as UserFilter

        bindViews()
        setFilterData(filter)

        selectedSigns = ArrayList()
        zodiacSignAdapter = ZodiacSignAdapter(selectedSigns, this)
        zodiacSignAdapter.displaySelected = false
        selectedSignsRV.adapter = zodiacSignAdapter
        selectedSignsRV.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        parent.setOnScrollChangeListener { _, _, _, _, _ ->
            minAge.correctOffsetWhenContainerOnScrolling()
            maxAge.correctOffsetWhenContainerOnScrolling()
            tolerance.correctOffsetWhenContainerOnScrolling()
        }

        male.setOnClickListener {
            if (!isMale) {
                maleOn.visibility = View.VISIBLE
                isMale = true
            } else {
                maleOn.visibility = View.GONE
                isMale = false
            }
        }

        female.setOnClickListener {
            if (!isFemale) {
                femaleOn.visibility = View.VISIBLE
                isFemale = true
            } else {
                femaleOn.visibility = View.GONE
                isFemale = false
            }
        }

        addSigns.setOnClickListener {
            val dialog = ZodiacSignDialog(this, this)
            dialog.create()

            dialog.setSelectedSigns(selectedSigns)

            dialog.show()

            dialog.setOnDismissListener {
                if (dialog.isDone) {

                    Log.d(TAG, "Dialog is done, received items -> ${dialog.getSelectedSigns().size}")
                    selectedSigns.clear()
                    for (index in dialog.getSelectedSigns().indices) {
                        val sign = dialog.getSelectedSigns()[index]
                        selectedSigns.add(sign)
                        zodiacSignAdapter.notifyItemInserted(index)

                        Log.d(TAG,"Added item -> ${sign.name}, Action: ${sign.action}, Selected: ${sign.selected}")
                    }

                    zodiacSignAdapter.notifyDataSetChanged()
                }
            }
        }

        toleranceInfo.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Tolerance info")
                setMessage("Tolerance will modify the interval that you choose for eat times. This increases" +
                        " the chances of finding someone in that interval")
                setNeutralButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }

        cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        done.setOnClickListener {

            if (city.text.isNullOrEmpty() || country.text.isNullOrEmpty()) {
                notifUtils.createToast("City and country fields can not be empty").show()
            } else if (minAge.progress > maxAge.progress) {
                notifUtils.createToast("Min age is higher than max age").show()
            } else if (start.text.isNullOrEmpty() || end.text.isNullOrEmpty()) {
                notifUtils.createToast("Start and end fields are empty").show()
            } else {
                filter.city = city.text.toString()
                filter.country = country.text.toString()
                filter.minAge = minAge.progress
                filter.maxAge = maxAge.progress
                filter.start = start.text.toString()
                filter.end = end.text.toString()
                filter.tolerance = tolerance.progress
                filter.zodiacSigns.apply {
                    clear()
                    // if no zodiac signs selected add all so the server looks for all zodiac signs
                    if (selectedSigns.isEmpty()) {
                        addAll(ZodiacSign.getList())
                    } else {
                        addAll(selectedSigns)
                    }
                }

                if (isMale && !isFemale) {
                    filter.gender = "Male"
                } else if (!isMale && isFemale) {
                    filter.gender = "Female"
                } else if (isMale && isFemale) {
                    filter.gender = "Both"
                }

                setResult(Activity.RESULT_OK, intent.putExtra("filter", filter))
                finish()
            }
        }
    }

    private fun bindViews() {
        parent = find(R.id.sv_parent)
        city = find(R.id.met_city)
        country = find(R.id.met_country)
        minAge = find(R.id.bsb_min_age)
        maxAge = find(R.id.bsb_max_age)
        addSigns = find(R.id.iv_add_signs)
        selectedSignsRV = find(R.id.rv_zodiac_signs)
        tolerance = find(R.id.bsb_tolerance)
        toleranceInfo = find(R.id.iv_tolerance_info)
        male = find(R.id.ll_male)
        maleOn = find(R.id.iv_male_on)
        female = find(R.id.ll_female)
        femaleOn = find(R.id.iv_female_on)
        start = find(R.id.met_start)
        end = find(R.id.met_end)
        cancel = find(R.id.fab_cancel)
        done = find(R.id.fab_done)
    }

    private fun setFilterData(filter: UserFilter) {
        city.setText(filter.city)
        country.setText(filter.country)
        start.setText(filter.start)
        end.setText(filter.end)
        minAge.setProgress(filter.minAge.toFloat())
        maxAge.setProgress(filter.maxAge.toFloat())
        tolerance.setProgress(filter.tolerance.toFloat())

        when {
            filter.gender.compareTo("Both") == 0 -> {
                maleOn.visibility = View.VISIBLE
                femaleOn.visibility = View.VISIBLE
                isMale = true
                isFemale = true
            }
            filter.gender.compareTo("Male") == 0 -> {
                maleOn.visibility = View.VISIBLE
                isMale = true
            }
            else -> {
                femaleOn.visibility = View.VISIBLE
                isFemale = true
            }
        }
    }
}
