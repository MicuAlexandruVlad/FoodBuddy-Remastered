package com.example.foodbuddyremastered.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.ViewPagerAdapter
import com.example.foodbuddyremastered.constants.ButtonIds
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ButtonPressedEvent
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.viewmodels.MainActivityViewModel
import com.google.firebase.messaging.FirebaseMessaging
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity() {

    private lateinit var  viewModel: MainActivityViewModel
    private lateinit var pager: ViewPager

    private lateinit var notifUtils: NotifUtils

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        notifUtils = NotifUtils(this)

        viewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]

        viewModel.currentUser = intent.getSerializableExtra("currentUser") as User

        FirebaseMessaging.getInstance().subscribeToTopic(viewModel.currentUser.id).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "Subscribed to topic -> ${viewModel.currentUser.id}")
            }
        }

        pager = find(R.id.pager)
        pager.adapter = ViewPagerAdapter(supportFragmentManager,
            // for conversation frag
            Bundle().apply {
                putSerializable("currentUser", viewModel.currentUser)
                putSerializable("conversations", intent.getSerializableExtra("conversations"))
                putSerializable("conversationIds", intent.getStringArrayListExtra("conversationIds"))
        },  // for discover frag
            Bundle().apply {
                putSerializable("currentUser", viewModel.currentUser)
        }, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onButtonEvent(buttonPressedEvent: ButtonPressedEvent) {
        when (buttonPressedEvent.buttonId) {
            ButtonIds.NAV_TO_DISCOVER -> pager.currentItem = 1
            ButtonIds.NAV_TO_CONVERSATIONS -> pager.currentItem = 0
        }
    }
}
