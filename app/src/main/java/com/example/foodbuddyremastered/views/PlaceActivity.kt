package com.example.foodbuddyremastered.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ReviewAdapter
import com.example.foodbuddyremastered.adapters.ScheduleAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.Place
import com.example.foodbuddyremastered.models.Review
import com.example.foodbuddyremastered.models.Schedule
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.JsonUtils
import com.example.foodbuddyremastered.viewmodels.PlaceViewModel
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.json.JSONArray

class PlaceActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaceViewModel

    private lateinit var placeImage: ImageView
    private lateinit var placeName: TextView
    private lateinit var rating: RatingBar
    private lateinit var visitors: TextView
    private lateinit var numReviews: TextView
    private lateinit var description: TextView
    private lateinit var scheduleHolder: LinearLayout
    private lateinit var reviewsRv: RecyclerView
    private lateinit var scheduleRv: RecyclerView
    private lateinit var addReview: ImageView
    private lateinit var noReviews: LinearLayout
    private lateinit var choosePlace: RelativeLayout

    private lateinit var place: Place
    private lateinit var currentUser: User
    private var position = -1
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var schedule: ArrayList<Schedule>
    private lateinit var reviewAdapter: ReviewAdapter
    private var liveResponse = MutableLiveData<ResponseEvent>()
    private var reviews = ArrayList<Review>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        place = intent.getSerializableExtra("place") as Place
        position = intent.getIntExtra("position", -1)
        currentUser = intent.getSerializableExtra("currentUser") as User


        viewModel = ViewModelProviders.of(this).get(PlaceViewModel::class.java)

        viewModel.context = this
        viewModel.owner = this

        bindViews()

        if (place.numReviews == 0) {
            noReviews.visibility = View.VISIBLE
        }

        reviewAdapter = ReviewAdapter(reviews, this)

        Glide.with(this).load(ApiUrls.getPlacePhoto(place.id, place.photoId[0])).into(placeImage)
        placeName.text = place.name
        rating.rating = place.rating.toFloat()
        numReviews.text = "(${place.numReviews})"
        visitors.text = "${place.visitors} people have been here"
        description.text = place.description

        reviewsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviewsRv.isNestedScrollingEnabled = false
        reviewsRv.adapter = reviewAdapter
        scheduleRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        scheduleRv.isNestedScrollingEnabled = false

        if (place.hasSchedule) {
            scheduleHolder.visibility = View.VISIBLE
            computeScheduleArray()
            scheduleAdapter = ScheduleAdapter(schedule, this)
            scheduleRv.adapter = scheduleAdapter
        } else {
            scheduleHolder.visibility = View.GONE
        }

        APIClient().getReviews(place.id, 3, liveResponse)

        liveResponse.observe(this, Observer<ResponseEvent> {
            if (it.action == Actions.RECEIVED_REVIEWS && it.status == HttpStatus.SC_OK) {
                val array = it.payload as JSONArray
                for (index in 0 until array.length()) {
                    reviews.add(JsonUtils.jsonObjectToReview(array.getJSONObject(index)))
                    reviewAdapter.notifyItemInserted(reviews.size - 1)
                }
            }
        })

        addReview.setOnClickListener {
            startActivityForResult(Intent(this, AddReviewActivity::class.java).apply {
                putExtra("placeId", place.id)
                putExtra("currentUser", currentUser)
            }, RequestCodes.ADD_REVIEW)
        }

        choosePlace.setOnClickListener {
            intent.putExtra("place", place)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun computeScheduleArray() {
        schedule = ArrayList<Schedule>().apply {
            add(Schedule().apply {
                day = "Monday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Tuesday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Wednesday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Thursday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Friday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Saturday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })

            add(Schedule().apply {
                day = "Sunday"
                start = place.schedule[day]!!.split(" ")[0]
                end = place.schedule[day]!!.split(" ")[1]
            })
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun bindViews() {
        placeImage = find(R.id.iv_place_image)
        placeName = find(R.id.tv_place_name)
        rating = find(R.id.rb_place_rating)
        numReviews = find(R.id.tv_num_reviews)
        visitors = find(R.id.tv_num_visitors)
        description = find(R.id.tv_place_description)
        scheduleHolder = find(R.id.ll_schedule_holder)
        reviewsRv = find(R.id.rv_place_reviews)
        scheduleRv = find(R.id.rv_place_schedule)
        addReview = find(R.id.iv_add_review)
        noReviews = find(R.id.ll_no_reviews)
        choosePlace = find(R.id.rl_choose_place)
    }
}
