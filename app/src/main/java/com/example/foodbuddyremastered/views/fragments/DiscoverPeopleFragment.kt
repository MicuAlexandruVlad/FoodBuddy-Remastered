package com.example.foodbuddyremastered.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.DiscoverAdapter
import com.example.foodbuddyremastered.constants.ButtonIds
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ButtonPressedEvent
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.viewmodels.MainActivityViewModel
import com.example.foodbuddyremastered.views.FilterActivity
import com.example.foodbuddyremastered.views.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class DiscoverPeopleFragment(val c: Context): Fragment() {
    companion object {
        const val TAG = "DiscoverPeopleFragment"
    }

    private lateinit var nextFragment: LinearLayout
    private lateinit var logOut: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var filter: FloatingActionButton

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var discoverAdapter: DiscoverAdapter
    private lateinit var foundUsers: ArrayList<User>
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        foundUsers = ArrayList()

        currentUser = arguments?.getSerializable("currentUser") as User

        discoverAdapter = DiscoverAdapter(foundUsers, context, currentUser)

        viewModel.lifecycleOwner = activity as LifecycleOwner

        viewModel.discoverUsers()
        viewModel.filteredUsers.observe(this, Observer<List<User>> { users ->
            Log.d(TAG, "Users found -> ${ users.size }")
            foundUsers.clear()
            foundUsers.addAll(users)
            discoverAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover_people, container, false)

        bindViews(view)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = discoverAdapter

        nextFragment.setOnClickListener {
            EventBus.getDefault().post(ButtonPressedEvent().apply {
                buttonId = ButtonIds.NAV_TO_CONVERSATIONS
            })
        }

        filter.setOnClickListener {
            activity?.startActivityForResult(Intent(context, FilterActivity::class.java).apply {
                putExtra("currentUser", currentUser)
            },
                RequestCodes.FILTER_ACTIVITY)
        }

        logOut.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle("Sign Out")
                .setMessage("You will be signed out. Are you sure you want to continue ?")
                .setNegativeButton("NO") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("YES") { dialogInterface, _ ->
                    doAsync {
                        Repository(context!!).apply {
                            val localUser = getAuthenticatedUser(true)[0]

                            FirebaseMessaging.getInstance().unsubscribeFromTopic(localUser.userId)

                            localUser.isAuthenticated = false
                            updateLocalUser(localUser)
                        }
                    }
                    dialogInterface.dismiss()
                    (c as Activity).startActivity(Intent(c, WelcomeActivity::class.java))
                    c.finish()
                }

            builder.create()
            builder.show()
        }

        return view
    }

    private fun bindViews(view: View) {
        nextFragment = view.find(R.id.ll_next_fragment)
        logOut = view.find(R.id.ll_log_out)
        recyclerView = view.find(R.id.rv_discover)
        filter = view.find(R.id.fab_filter)
    }
}