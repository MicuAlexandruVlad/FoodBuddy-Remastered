package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ZodiacSignAdapter
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.example.foodbuddyremastered.models.ZodiacSign
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.views.dialogs.NewFilterDialog
import com.example.foodbuddyremastered.views.dialogs.ZodiacSignDialog
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import com.xw.repo.BubbleSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find


class FilterViewModel(app: Application,
                      private val owner:LifecycleOwner,
                      private val context: Context)
    : ObservableViewModel(app) {

    companion object {
        const val TAG = "FilterViewModel"
    }

    lateinit var currentUser: User

    private var activeFilter = UserFilter()
    private var spinnerData = ArrayList<String>()
    private var liveActiveFilter = MutableLiveData<UserFilter>()
    private var filters = ArrayList<UserFilter>()
    private lateinit var zodiacSignAdapter: ZodiacSignAdapter
    private lateinit var selectedSigns: ArrayList<ZodiacSign>

    private var isMale = false
    private var isFemale = false

    private lateinit var body: NestedScrollView
    private lateinit var filterSelector: Spinner
    private lateinit var selectedSignsRV: RecyclerView
    private lateinit var femaleOn: ImageView
    private lateinit var maleOn: ImageView
    private lateinit var minAge: BubbleSeekBar
    private lateinit var maxAge: BubbleSeekBar
    private lateinit var tolerance: BubbleSeekBar
    private lateinit var toleranceInfo: ImageView
    private lateinit var start: MaterialEditText
    private lateinit var end: MaterialEditText
    private lateinit var city: MaterialEditText
    private lateinit var country: MaterialEditText

    private val notifUtils = NotifUtils(context)

    fun initView() {
        bindViews()

        body.setOnScrollChangeListener { _, _, _, _, _ ->
            minAge.correctOffsetWhenContainerOnScrolling()
            maxAge.correctOffsetWhenContainerOnScrolling()
            tolerance.correctOffsetWhenContainerOnScrolling()
        }

        selectedSigns = ArrayList()
        zodiacSignAdapter = ZodiacSignAdapter(selectedSigns, context)
        zodiacSignAdapter.displaySelected = false
        selectedSignsRV.adapter = zodiacSignAdapter

        selectedSignsRV.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        val dataAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            spinnerData
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSelector.adapter = dataAdapter


        filterSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                bindFilterData()
                activeFilter.isLastUsed = false
                updateFilter(activeFilter)

                val filterName = spinnerData[position]

                for (index in filters.indices) {
                    if (filters[index].name.compareTo(filterName) == 0) {
                        filters[index].isLastUsed = true

                        updateFilter(filters[index])

                        break
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        Repository(context).apply {
            getActiveFilterLive(currentUser.id, true).observe(owner, Observer<List<UserFilter>> {
                if (it.isNotEmpty() && it.size == 1) {
                    activeFilter = it[0]
                    Log.d(TAG, "Active filter -> ${Gson().toJson(it)}")

                    updateUi(activeFilter)
                }
            })

            getFilterNamesLive(currentUser.id).observe(owner, Observer<List<String>> {
                Log.d(TAG, "Filter names -> ${Gson().toJson(it)}")
                if (spinnerData.isNotEmpty()) {
                    spinnerData.clear()
                }

                spinnerData.addAll(it)
                dataAdapter.notifyDataSetChanged()
            })

            getAllFilters(currentUser.id).observe(owner, Observer<List<UserFilter>> {
                if (filters.isNotEmpty()) {
                    filters.clear()
                }

                filters.addAll(it)
            })
        }
    }

    private fun updateUi(activeFilter: UserFilter) {
        city.setText(activeFilter.city)
        country.setText(activeFilter.country)
        start.setText(activeFilter.start)
        end.setText(activeFilter.end)
        minAge.setProgress(activeFilter.minAge.toFloat())
        maxAge.setProgress(activeFilter.maxAge.toFloat())
        tolerance.setProgress(activeFilter.tolerance.toFloat())

        when {
            activeFilter.gender.compareTo("Both") == 0 -> {
                maleOn.visibility = View.VISIBLE
                femaleOn.visibility = View.VISIBLE
                isMale = true
                isFemale = true
            }
            activeFilter.gender.compareTo("Male") == 0 -> {
                maleOn.visibility = View.VISIBLE
                isMale = true
            }
            else -> {
                femaleOn.visibility = View.VISIBLE
                isFemale = true
            }
        }

        selectedSigns.clear()
        selectedSigns.addAll(activeFilter.zodiacSigns)
        zodiacSignAdapter.notifyDataSetChanged()


    }

    fun onUserFemale() {
        if (!isFemale) {
            femaleOn.visibility = View.VISIBLE
            isFemale = true
        } else {
            femaleOn.visibility = View.GONE
            isFemale = false
        }
    }

    fun onUserMale() {
        if (!isMale) {
            maleOn.visibility = View.VISIBLE
            isMale = true
        } else {
            maleOn.visibility = View.GONE
            isMale = false
        }
    }

    fun onNewFilter() {
        val dialog = NewFilterDialog(context)

        dialog.create()
        dialog.show()

        dialog.setOnDismissListener {
            if (dialog.isDone) {
                val name = dialog.name

                createNewFilter(name)
            }
        }
    }

    private fun createNewFilter(name: String) {
        UserFilter().apply {
            this.name = name
            type = UserFilter.CUSTOM_FILTER
            start = ""
            end = ""
            city = ""
            country = ""
            gender = "Both"
            isLastUsed = true
            ownerId = currentUser.id
            zodiacSigns.addAll(ZodiacSign.getList())

            updateUi(this)
            insertFilter(this)
            activeFilter.isLastUsed = false
            updateFilter(activeFilter)
        }
    }

    private fun insertFilter(userFilter: UserFilter) {
        doAsync {
            Repository(context).insertFilter(userFilter)
        }
    }

    private fun updateFilter(userFilter: UserFilter) {
        doAsync {
            Repository(context).updateFilter(userFilter)
        }
    }

    fun onRemoveFilter() {
        if (activeFilter.type == UserFilter.DEFAULT_FILTER) {
            Toast.makeText(context, "Default filter can not be deleted", Toast.LENGTH_SHORT).show()
        } else {
            val builder = AlertDialog.Builder(context)
                .setTitle("Delete filter")
                .setMessage("You are about to delete this filter. Are you sure ?")
                .setNegativeButton("NO") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("YES") { dialogInterface, _ ->
                    doAsync { Repository(context).removeFilter(activeFilter.id!!) }
                    dialogInterface.dismiss()
                }

            builder.create()
            builder.show()
        }
    }

    fun onAddSigns() {
        val dialog = ZodiacSignDialog(owner, context)
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

    fun onToleranceInfo() {
        AlertDialog.Builder(context).apply {
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

    fun onCancel() {
        with(context as Activity) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    fun onDone() {
        if (city.text!!.isEmpty() || country.text!!.isEmpty()) {
            notifUtils.createToast("City and country fields can not be empty").show()
        } else if (minAge.progress > maxAge.progress) {
            notifUtils.createToast("Min age is higher than max age").show()
        } else if (start.text!!.isEmpty() || end.text!!.isEmpty()) {
            notifUtils.createToast("Start and end fields are empty").show()
        } else {
            bindFilterData()

            activeFilter.isLastUsed = true

            doAsync { Repository(context).updateFilter(activeFilter) }

            with(context as Activity) {
                finish()
            }
        }
    }

    private fun bindFilterData() {
        activeFilter.city = city.text.toString()
        activeFilter.country = country.text.toString()
        activeFilter.minAge = minAge.progress
        activeFilter.maxAge = maxAge.progress
        activeFilter.start = start.text.toString()
        activeFilter.end = end.text.toString()
        activeFilter.tolerance = tolerance.progress
        activeFilter.zodiacSigns.apply {
            clear()
            // if no zodiac signs selected add all so the server looks for all zodiac signs
            if (selectedSigns.isEmpty()) {
                addAll(ZodiacSign.getList())
            } else {
                addAll(selectedSigns)
            }
        }

        if (isMale && !isFemale) {
            activeFilter.gender = "Male"
        } else if (!isMale && isFemale) {
            activeFilter.gender = "Female"
        } else if (isMale && isFemale) {
            activeFilter.gender = "Both"
        }
    }

    private fun bindViews() {
        context as Activity
        with(context) {
            body = find(R.id.sv_parent)
            filterSelector = find(R.id.sp_filter)
            selectedSignsRV = find(R.id.rv_zodiac_signs)
            maleOn = find(R.id.iv_male_on)
            femaleOn = find(R.id.iv_female_on)
            minAge = find(R.id.bsb_min_age)
            maxAge = find(R.id.bsb_max_age)
            tolerance = find(R.id.bsb_tolerance)
            toleranceInfo = find(R.id.iv_tolerance_info)
            city = find(R.id.met_city)
            country = find(R.id.met_country)
            start = find(R.id.met_start)
            end = find(R.id.met_end)
        }
    }
}