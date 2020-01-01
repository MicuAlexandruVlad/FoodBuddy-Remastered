package com.example.foodbuddyremastered

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.NotifUtils
import com.rengwuxian.materialedittext.MaterialEditText
import com.xw.repo.BubbleSeekBar
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import kotlin.math.sign

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

    private var isMale = false
    private var isFemale = false
    private var isPartnerMale = false
    private var isPartnerFemale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        bindViews()
        //EventBus.getDefault().register(this)
        val notifUtils = NotifUtils(this)

        eatTimesArray = ArrayList()
        eatTimesAdapter = EatTimesAdapter(eatTimesArray, this, User())
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

            isPartnerMale = false
            isPartnerFemale = true

            Log.d(TAG, "isPartnerMale -> $isPartnerMale")
        }

        addEatTimes.setOnClickListener {
            eatTimesArray.add(EatTimes())
            eatTimesAdapter.notifyItemInserted(eatTimesArray.size - 1)
        }
    }

    private fun bindViews() {
        parent = find(R.id.sv_parent)
        email = find(R.id.met_email)
        password = find(R.id.met_password)
        confirmPassword = find(R.id.met_confirm_password)
        firstName = find(R.id.met_first_name)
        lastName = find(R.id.met_last_name)
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
        //EventBus.getDefault().unregister(this)
    }
}
