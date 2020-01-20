package com.example.foodbuddyremastered.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.EatTimesAdapter
import com.example.foodbuddyremastered.adapters.ZodiacSignAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.models.CompressedImage
import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.ZodiacSign
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.ImageUtils
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.views.SignUpActivity
import com.example.foodbuddyremastered.views.dialogs.PhotoOptionPickerDialog
import com.example.foodbuddyremastered.views.dialogs.ZodiacSignDialog
import com.rengwuxian.materialedittext.MaterialEditText
import com.xw.repo.BubbleSeekBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList


class SignUpViewModel(app: Application, private val context: Context):
    ObservableViewModel(app) {

    var user = User()

    private lateinit var parent: ScrollView
    private lateinit var genderMale: RelativeLayout
    private lateinit var genderFemale: RelativeLayout
    private lateinit var partnerGenderFemale: RelativeLayout
    private lateinit var partnerGenderMale: RelativeLayout
    private lateinit var maleOn: ImageView
    private lateinit var femaleOn: ImageView
    private lateinit var partnerMaleOn: ImageView
    private lateinit var partnerFemaleOn: ImageView
    private lateinit var userAge: BubbleSeekBar
    private lateinit var partnerMinAge: BubbleSeekBar
    private lateinit var partnerMaxAge: BubbleSeekBar
    private lateinit var addSign: ImageView
    private lateinit var zodiacSignRv: RecyclerView
    private lateinit var addEatTimes: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userPhoto: ImageView
    private lateinit var photoButtonIcon: ImageView
    private lateinit var photoBtnTextView: TextView


    private val notifUtils = NotifUtils(context)
    private lateinit var eatTimesArray: ArrayList<EatTimes>
    private lateinit var eatTimesAdapter: EatTimesAdapter
    private lateinit var selectedSigns: ArrayList<ZodiacSign>
    private lateinit var zodiacSignAdapter: ZodiacSignAdapter
    private lateinit var compressedImage: CompressedImage

    private var isMale = false
    private var isFemale = false
    private var isPartnerMale = false
    private var hasPhoto = false
    private var isPartnerFemale = false

    var confirmPass = ""

    @SuppressLint("SetTextI18n")
    fun create() {

        val owner = context as Activity

        parent = owner.find(R.id.sv_parent)
        genderMale = owner.find(R.id.ll_male)
        genderFemale = owner.find(R.id.ll_female)
        partnerGenderFemale = owner.find(R.id.rl_partner_female)
        partnerGenderMale = owner.find(R.id.rl_partner_male)
        maleOn = owner.find(R.id.iv_male_on)
        femaleOn = owner.find(R.id.iv_female_on)
        partnerMaleOn = owner.find(R.id.iv_partner_male_on)
        partnerFemaleOn = owner.find(R.id.iv_partner_female_on)
        userAge = owner.find(R.id.bsb_age)
        partnerMinAge = owner.find(R.id.bsb_partner_min_age)
        partnerMaxAge = owner.find(R.id.bsb_partner_max_age)
        addSign = owner.find(R.id.iv_add_zodiac_sign)
        zodiacSignRv = owner.find(R.id.rv_selected_sign)
        addEatTimes = owner.find(R.id.iv_add_eat_times)
        recyclerView = owner.find(R.id.rv_eat_times)
        userPhoto = owner.find(R.id.iv_user_photo)
        photoButtonIcon = owner.find(R.id.iv_photo_btn_icon)
        photoBtnTextView = owner.find(R.id.tv_btn_text)

        photoBtnTextView.text = "Add Photo"

        selectedSigns = ArrayList()
        zodiacSignAdapter = ZodiacSignAdapter(selectedSigns, context)
        zodiacSignAdapter.displaySelected = false
        zodiacSignRv.adapter = zodiacSignAdapter
        zodiacSignRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        eatTimesArray = ArrayList()
        eatTimesAdapter =
            EatTimesAdapter(
                eatTimesArray,
                context,
                User()
            )
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = eatTimesAdapter
        recyclerView.isNestedScrollingEnabled = false

        parent.setOnScrollChangeListener { _, _, _, _, _ ->
            userAge.correctOffsetWhenContainerOnScrolling()
            partnerMinAge.correctOffsetWhenContainerOnScrolling()
            partnerMaxAge.correctOffsetWhenContainerOnScrolling()
        }


        genderMale.setOnClickListener {
            if (!isMale && !isFemale) {
                maleOn.visibility = View.VISIBLE
            } else if (!isMale && isFemale) {
                maleOn.visibility = View.VISIBLE
                femaleOn.visibility = View.GONE
            }

            isMale = true
            isFemale = false

            user.gender = "Male"
        }

        genderFemale.setOnClickListener {

            if (!isMale && !isFemale) {
                femaleOn.visibility = View.VISIBLE
            } else if (!isFemale && isMale) {
                femaleOn.visibility = View.VISIBLE
                maleOn.visibility = View.GONE
            }

            isMale = false
            isFemale = true

            user.gender = "Female"
        }

        partnerGenderMale.setOnClickListener {

            if (!isPartnerMale && !isPartnerFemale) {
                partnerMaleOn.visibility = View.VISIBLE
            } else if (!isMale && isFemale) {
                partnerMaleOn.visibility = View.VISIBLE
                partnerFemaleOn.visibility = View.GONE
            }

            isPartnerMale = true
            isPartnerFemale = false

            user.partnerGender = "Male"
        }

        partnerGenderFemale.setOnClickListener {

            if (!isPartnerMale && !isPartnerFemale) {
                partnerFemaleOn.visibility = View.VISIBLE
            } else if (!isPartnerFemale && isPartnerMale) {
                partnerFemaleOn.visibility = View.VISIBLE
                partnerMaleOn.visibility = View.GONE
            }

            isPartnerMale = false
            isPartnerFemale = true

            user.partnerGender = "Female"
        }

        addEatTimes.setOnClickListener {
            eatTimesArray.add(EatTimes())
            eatTimesAdapter.notifyItemInserted(eatTimesArray.size - 1)
        }

        addSign.setOnClickListener {
            val dialog = ZodiacSignDialog(context as LifecycleOwner, context)
            dialog.pickOne = true

            dialog.create()
            dialog.show()


            dialog.setOnDismissListener {
                if (dialog.isDone) {
                    selectedSigns.clear()
                    selectedSigns.addAll(dialog.getSelectedSigns())

                    zodiacSignAdapter.notifyDataSetChanged()

                    if (selectedSigns.size > 0) {
                        user.zodiacSign = selectedSigns[0].name
                    }

                    Log.d(SignUpActivity.TAG, "Zodiac signs received -> ${ selectedSigns.size }")
                }
            }

        }
    }

    private fun uploadUser() {
        val apiClient = APIClient()
        apiClient.registerUserEmail(user, context)
    }

    fun onSignUp() {
        if (!checkFieldsCompletion()) {
            notifUtils.createToast("One or more fields are empty").show()
        } else if (!isMale && !isFemale) {
            notifUtils.createToast("User gender not selected").show()
        } else if (user.password.compareTo(confirmPass) != 0) {
            notifUtils.createToast("Passwords are not matching")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            notifUtils.createToast("Email address is not valid").show()
        } else if (!isPartnerFemale && !isPartnerMale) {
            notifUtils.createToast("Partner gender not selected")
        } else if (user.zodiacSign.isEmpty()) {
            notifUtils.createToast("Zodiac sign not selected").show()
        } else if (!checkEatTimes(user)) {
            notifUtils.createToast("Eat times not added").show()
        } else if(!hasPhoto){
            notifUtils.createToast("No photo added").show()
        } else {
            user.age = userAge.progress
            user.partnerMinAge = partnerMinAge.progress
            user.partnerMaxAge = partnerMaxAge.progress
            user.compressedImage = compressedImage
            uploadUser()
        }
    }

    fun onAddPhoto() {
        val dialog = PhotoOptionPickerDialog(context)

        dialog.create()
        dialog.show()

        dialog.setOnDismissListener {
            if (dialog.isDone) {
                when (dialog.option) {
                    Actions.CAMERA_PICKED -> {
                        launchCameraIntent()
                    }

                    Actions.GALLERY_PICKED -> {
                        launchGalleryIntent()
                    }
                }
            }
        }
    }

    private fun launchCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            (context as Activity).startActivityForResult(takePictureIntent, RequestCodes.LAUNCH_CAMERA)
        }
    }

    @SuppressLint("SetTextI18n")
    fun displayPhoto(bitmap: Bitmap) {

        hasPhoto = true

        photoBtnTextView.text = "Change Photo"
        photoButtonIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.change))
        userPhoto.setImageBitmap(bitmap)
        doAsync {
            compressedImage = ImageUtils.compressImage(bitmap, 40)
            compressedImage.imageName = "JPEG_${Calendar.getInstance().time.time}"
        }
    }

    private fun launchGalleryIntent() {
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        (context as Activity).startActivityForResult(pickIntent, RequestCodes.LAUNCH_GALLERY)
    }

    private fun checkFieldsCompletion(): Boolean {
        if (user.email.isEmpty() || user.password.isEmpty() || user.firstName.isEmpty() ||
            user.lastName.isEmpty() || user.city.isEmpty() || user.country.isEmpty()
        ) {
            return false
        }
        return true
    }

    private fun checkEatTimes(user: User): Boolean {
        user.eatTimes.clear()

        if (recyclerView.childCount == 0) {
            return false
        }

        for (index in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(index)
            val start = child.find<MaterialEditText>(R.id.met_start)
            val end = child.find<MaterialEditText>(R.id.met_end)

            if (start.text.isNullOrEmpty() || end.text.isNullOrEmpty()) {
                return false
            } else {
                val eatTimes = EatTimes()
                eatTimes.start = start.text.toString()
                eatTimes.end = end.text.toString()

                user.eatTimes.add(eatTimes)
            }
        }

        return true
    }

}