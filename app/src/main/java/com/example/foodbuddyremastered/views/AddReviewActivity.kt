package com.example.foodbuddyremastered.views

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.Review
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.find
import java.util.*

class AddReviewActivity : AppCompatActivity() {

    private lateinit var placeId: String
    private lateinit var currentUser: User

    private lateinit var content: EditText
    private lateinit var publish: RelativeLayout
    private lateinit var ratingBar: RatingBar
    private var review = Review()

    private val liveResponse = MutableLiveData<ResponseEvent>()

    private val notifUtils = NotifUtils(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        content = find(R.id.et_review_text)
        publish = find(R.id.rl_publish_review)
        ratingBar = find(R.id.rb_place_rating)

        placeId = intent.getStringExtra("placeId") as String
        currentUser = intent.getSerializableExtra("currentUser") as User


        liveResponse.observe(this, Observer<ResponseEvent> {
            if (it.action == Actions.REVIEW_UPLOADED) {
                if (it.status == HttpStatus.SC_OK) {
                    intent.putExtra("review", review)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        })

        publish.setOnClickListener {
            if (ratingBar.rating < 0.5) {
                notifUtils.createToast("Place not rated").show()
            } else {
                review.content = content.text.toString()
                review.rating = ratingBar.rating.toDouble()
                review.userId = currentUser.id
                review.timestamp = Calendar.getInstance().time.toString()
                review.userName = "${currentUser.firstName} ${currentUser.lastName}"
                review.placeId = placeId

                APIClient().registerReview(review, liveResponse)
            }
        }
    }


}
