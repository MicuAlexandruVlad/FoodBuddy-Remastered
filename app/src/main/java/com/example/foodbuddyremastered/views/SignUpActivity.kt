package com.example.foodbuddyremastered.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.EatTimesAdapter
import com.example.foodbuddyremastered.constants.ObjectTypes
import com.example.foodbuddyremastered.events.ObjectUploadedEvent
import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import com.xw.repo.BubbleSeekBar
import cz.msebera.android.httpclient.HttpStatus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find


class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var parent: ScrollView
    private lateinit var email: MaterialEditText
    private lateinit var password: MaterialEditText
    private lateinit var confirmPassword: MaterialEditText
    private lateinit var firstName: MaterialEditText
    private lateinit var lastName: MaterialEditText
    private lateinit var phoneNumber: MaterialEditText
    private lateinit var city: MaterialEditText
    private lateinit var country: MaterialEditText
    private lateinit var userAge: BubbleSeekBar
    private lateinit var genderMale: RelativeLayout
    private lateinit var maleOn: ImageView
    private lateinit var maleOff: ImageView
    private lateinit var genderFemale: RelativeLayout
    private lateinit var femaleOn: ImageView
    private lateinit var femaleOff: ImageView
    private lateinit var partnerGenderMale: RelativeLayout
    private lateinit var partnerMaleOn: ImageView
    private lateinit var partnerMaleOff: ImageView
    private lateinit var partnerGenderFemale: RelativeLayout
    private lateinit var partnerFemaleOn: ImageView
    private lateinit var partnerFemaleOff: ImageView
    private lateinit var partnerMinAge: BubbleSeekBar
    private lateinit var partnerMaxAge: BubbleSeekBar
    private lateinit var addEatTimes: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var signUp: Button

    private lateinit var eatTimesArray: ArrayList<EatTimes>
    private lateinit var eatTimesAdapter: EatTimesAdapter
    private lateinit var notifUtils: NotifUtils

    private var isMale = false
    private var isFemale = false
    private var isGenderSelected = false
    private var isPartnerMale = false
    private var isPartnerFemale = false
    private var isPartnerGenderSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        bindViews()
        EventBus.getDefault().register(this)
        notifUtils = NotifUtils(this)




        eatTimesArray = ArrayList()
        eatTimesAdapter =
            EatTimesAdapter(
                eatTimesArray,
                this,
                User()
            )
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

            isGenderSelected = true
            isMale = true
            isFemale = false

            Log.d(TAG, "isMale -> $isMale")
        }

        genderFemale.setOnClickListener {

            if (!isMale && !isFemale) {
                femaleOn.visibility = View.VISIBLE
            } else if (!isFemale && isMale) {
                femaleOn.visibility = View.VISIBLE
                maleOn.visibility = View.GONE
            }

            isGenderSelected = true
            isMale = false
            isFemale = true

            Log.d(TAG, "isMale -> $isMale")
        }

        partnerGenderMale.setOnClickListener {

            if (!isPartnerMale && !isPartnerFemale) {
                partnerMaleOn.visibility = View.VISIBLE
            } else if (!isMale && isFemale) {
                partnerMaleOn.visibility = View.VISIBLE
                partnerFemaleOn.visibility = View.GONE
            }

            isPartnerGenderSelected = true
            isPartnerMale = true
            isPartnerFemale = false

            Log.d(TAG, "isPartnerMale -> $isPartnerMale")
        }

        partnerGenderFemale.setOnClickListener {

            if (!isPartnerMale && !isPartnerFemale) {
                partnerFemaleOn.visibility = View.VISIBLE
            } else if (!isPartnerFemale && isPartnerMale) {
                partnerFemaleOn.visibility = View.VISIBLE
                partnerMaleOn.visibility = View.GONE
            }

            isPartnerGenderSelected = true
            isPartnerMale = false
            isPartnerFemale = true

            Log.d(TAG, "isPartnerMale -> $isPartnerMale")
        }

        addEatTimes.setOnClickListener {
            eatTimesArray.add(EatTimes())
            eatTimesAdapter.notifyItemInserted(eatTimesArray.size - 1)
        }

        signUp.setOnClickListener {
            val user = User()
            if (checkFieldsCompletion()) {
                if (!isGenderSelected) {
                    notifUtils.createToast("Gender not selected").show()
                } else if (!isPartnerGenderSelected) {
                    notifUtils.createToast("Partner gender not selected").show()
                } else if (password.text.toString().compareTo(confirmPassword.text.toString()) != 0) {
                    notifUtils.createToast("Passwords do not match").show()
                } else if (eatTimesArray.isEmpty()) {
                    notifUtils.createToast("No eat times added").show()
                } else if (checkEatTimes(user)) {
                    user.email = email.text.toString()
                    user.password = password.text.toString()
                    user.firstName = firstName.text.toString()
                    user.lastName = lastName.text.toString()
                    user.phoneNumber = phoneNumber.text.toString()
                    user.city = city.text.toString()
                    user.country = country.text.toString()
                    user.age = userAge.progress
                    user.partnerMinAge = partnerMinAge.progress
                    user.partnerMaxAge = partnerMaxAge.progress
                    user.profileComplete = true

                    if (isMale) {
                        user.gender = "Male"
                    } else {
                        user.gender = "Female"
                    }

                    if (isPartnerMale) {
                        user.partnerGender = "Male"
                    } else {
                        user.partnerGender = "Female"
                    }

                    Log.d(TAG, "User object -> ${Gson().toJson(user)}")

                    val apiClient = APIClient()
                    apiClient.registerUserEmail(user)

                } else {
                    notifUtils.createToast("Some fields are empty").show()
                }
            } else {
                notifUtils.createToast("One or more fields are empty").show()
            }
        }
    }

    private fun checkFieldsCompletion(): Boolean {

        if (email.text.isNullOrEmpty() || password.text.isNullOrEmpty() || confirmPassword.text.isNullOrEmpty()
            || firstName.text.isNullOrEmpty() || lastName.text.isNullOrEmpty() || city.text.isNullOrEmpty()
            || country.text.isNullOrEmpty()) {

            return false
        }

        return true
    }

    private fun bindViews() {
        parent = find(R.id.sv_parent)
        email = find(R.id.met_email)
        password = find(R.id.met_password)
        confirmPassword = find(R.id.met_confirm_password)
        firstName = find(R.id.met_first_name)
        lastName = find(R.id.met_last_name)
        phoneNumber = find(R.id.met_phone_number)
        city = find(R.id.met_city)
        country = find(R.id.met_country)
        userAge = find(R.id.bsb_age)
        genderMale = find(R.id.ll_male)
        maleOn = find(R.id.iv_male_on)
        maleOff = find(R.id.iv_male_off)
        genderFemale = find(R.id.ll_female)
        femaleOn = find(R.id.iv_female_on)
        femaleOff = find(R.id.iv_female_off)
        partnerGenderMale = find(R.id.rl_partner_male)
        partnerMaleOn = find(R.id.iv_partner_male_on)
        partnerMaleOff = find(R.id.iv_partner_male_off)
        partnerGenderFemale = find(R.id.rl_partner_female)
        partnerFemaleOn = find(R.id.iv_partner_female_on)
        partnerFemaleOff = find(R.id.iv_partner_female_off)
        partnerMinAge = find(R.id.bsb_partner_min_age)
        partnerMaxAge = find(R.id.bsb_partner_max_age)
        addEatTimes = find(R.id.iv_add_eat_times)
        recyclerView = find(R.id.rv_eat_times)
        signUp = find(R.id.btn_sign_up)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun checkEatTimes(user: User): Boolean {
        user.eatTimes.clear()

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserRegistered(objectUploadedEvent: ObjectUploadedEvent) {
        if (objectUploadedEvent.objType == ObjectTypes.USER) {
            when (objectUploadedEvent.status) {
                HttpStatus.SC_CREATED -> {
                    notifUtils.createToast("Account created").show()
                    Intent(this, LoginActivity::class.java).also {
                        finish()
                        startActivity(it)
                    }
                }
            }
        }
    }
}
