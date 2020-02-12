package com.example.foodbuddyremastered.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.PlacePhotoAdapter
import com.example.foodbuddyremastered.adapters.PlaceTypeAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.*
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.DisplayUtils
import com.example.foodbuddyremastered.utils.ImageUtils
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.views.dialogs.PhotoPickerDialog
import com.google.gson.Gson
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList

// TODO: maybe make photos optional

class AddPlaceViewModel(app: Application,
                        private val context: Context,
                        private val owner: LifecycleOwner,
                        private val currentUser: User
                        )
    : ObservableViewModel(app) {

    companion object {
        const val TAG = "AddPlaceViewModel"
    }

    private lateinit var scheduleHolder: LinearLayout
    private lateinit var typeRv: RecyclerView
    private lateinit var photosRv: RecyclerView
    private lateinit var place: Place

    private var hasSchedule = false
    private var hasPhoto = false
    private val notifUtils = NotifUtils(context)
    private lateinit var photos: ArrayList<PlaceImage>
    private val liveResponse = MutableLiveData<ResponseEvent>()

    var name = ""
    var city = ""
    var country = ""
    var description = ""
    var street = ""
    var zip = ""
    var mondayStart = ""
    var mondayEnd = ""
    var tuesdayStart = ""
    var tuesdayEnd = ""
    var wednesdayStart = ""
    var wednesdayEnd = ""
    var thursdayStart = ""
    var thursdayEnd = ""
    var fridayStart = ""
    var fridayEnd = ""
    var saturdayStart = ""
    var saturdayEnd = ""
    var sundayStart = ""
    var sundayEnd = ""

    private lateinit var typeAdapter: PlaceTypeAdapter
    private lateinit var photoAdapter: PlacePhotoAdapter
    private lateinit var types: ArrayList<PlaceType>
    private lateinit var compressedImages: ArrayList<CompressedImage>

    private lateinit var placeId: String


    fun initView() {
        bindViews()

        liveResponse.observe(owner, Observer<ResponseEvent> {
            when (it.action) {
                Actions.PLACE_REGISTERED -> {
                    if (it.status == HttpStatus.SC_OK) {
                        val id = it.payload as String
                        placeId = id

                        Log.d(TAG, "Place registered with id -> $placeId")

                        APIClient().uploadPlaceImage(compressedImages[0], liveResponse, id, 0)
                    }
                }

                Actions.PLACE_IMAGE_UPLOADED -> {
                    if (it.status == HttpStatus.SC_CREATED) {
                        var lastIndex = it.payload as Int
                        lastIndex++

                        place.photoId.add(it.payload2.toString())

                        if (lastIndex < compressedImages.size) {
                            APIClient().uploadPlaceImage(
                                compressedImages[lastIndex],
                                liveResponse,
                                placeId,
                                lastIndex
                            )
                        } else {
                            // TODO: add a loading bar and stop it here

                            with(context as Activity) {
                                setResult(Activity.RESULT_OK, Intent().apply {
                                    putExtra("place", place)
                                })
                                finish()
                            }
                        }
                    }
                }
            }
        })

        photos = ArrayList()
        compressedImages = ArrayList()

        types = PlaceType.getItems(context)
        typeAdapter = PlaceTypeAdapter(types, context)
        typeRv.layoutManager = GridLayoutManager(context, DisplayUtils.calculateNumberOfColumns(context, 172f))
        typeRv.adapter = typeAdapter

        photoAdapter = PlacePhotoAdapter(photos, context, currentUser)
        photosRv.adapter = photoAdapter
        photosRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun onSchedule() {
        hasSchedule = !hasSchedule

        scheduleHolder.visibility = if (hasSchedule) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun onAddPhoto() {
        val dialog = PhotoPickerDialog(context)

        dialog.create()
        dialog.show()

        dialog.setOnDismissListener {
            if (dialog.isDone) {
                Log.d(TAG, "Option -> ${dialog.option}")
                when (dialog.option) {
                    0 -> launchCameraIntent()

                    1 -> launchGalleryIntent()

                    2 -> launchWebIntent()
                }
            }
        }
    }

    private fun launchWebIntent() {

    }

    private fun launchCameraIntent() {

    }

    private fun launchGalleryIntent() {
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        (context as Activity).startActivityForResult(pickIntent, RequestCodes.LAUNCH_GALLERY)
    }

    fun displayPhoto(bitmap: Bitmap, location: Int) {
        if (!hasPhoto) {
            hasPhoto = true
        }

        photos.add(PlaceImage().apply {
            val time = getUnixTime()

            name = time
            signature = time
            data = bitmap
            this.location = location

        })

        photoAdapter.notifyItemInserted(photos.size - 1)

        doAsync {
            Log.d(TAG, "Compressing image")
            compressedImages.add(ImageUtils.compressImageNew(bitmap, 40, location))
            Log.d(TAG, "Compressed image name -> ${compressedImages[compressedImages.size - 1].imageName}")
            Log.d(TAG, "Compressed images -> ${compressedImages.size}")
        }
    }

    private fun getUnixTime(): String {
        return Calendar.getInstance().time.time.toString()
    }

    fun onAddPlace() {
        place = Place()
        place.photoId = ArrayList()
        if (name.isEmpty() || city.isEmpty() || country.isEmpty() || street.isEmpty() || zip.isEmpty()) {
            notifUtils.createToast("One or more fields are empty").show()
        } else if (!typeAdapter.typeSelected) {
            notifUtils.createToast("Place type is not selected").show()
        } else if (!hasPhoto){
            notifUtils.createToast("No photo added").show()
        } else {
            place.name = name
            place.description = description
            place.address = Address().also {
                it.city = city
                it.country = country
                it.postalCode = zip
                it.street = street
            }
            place.hasSchedule = hasSchedule
            place.schedule = computeSchedule()
            place.placeType = typeAdapter.selectedPlaceType.name

            place.createdBy = currentUser.id
            place.lastEditedBy = currentUser.id

            Log.d(TAG, "Place data -> ${Gson().toJson(place)}")

            APIClient().registerPlace(place, liveResponse)
        }
    }

    private fun computeSchedule(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("Monday", "$mondayStart $mondayEnd")
            put("Tuesday", "$tuesdayStart $tuesdayEnd")
            put("Wednesday", "$wednesdayStart $wednesdayEnd")
            put("Thursday", "$thursdayStart $thursdayEnd")
            put("Friday", "$fridayStart $fridayEnd")
            put("Saturday", "$saturdayStart $saturdayEnd")
            put("Sunday", "$sundayStart $sundayEnd")
        }
    }

    private fun bindViews() {
        with(context as Activity) {
            scheduleHolder = find(R.id.ll_schedule_holder)
            typeRv = find(R.id.rv_place_types)
            photosRv = find(R.id.rv_place_photos)
        }
    }
}