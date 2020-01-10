package com.example.foodbuddyremastered.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.DiscoverAdapter
import com.example.foodbuddyremastered.constants.ButtonIds
import com.example.foodbuddyremastered.constants.RequestCodes
import com.example.foodbuddyremastered.events.ButtonPressedEvent
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.viewmodels.MainActivityViewModel
import com.example.foodbuddyremastered.views.FilterActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import java.lang.Exception

class DiscoverPeopleFragment: Fragment() {
    companion object {
        const val TAG = "DiscoverPeopleFragment"
    }

    private lateinit var nextFragment: LinearLayout
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
                putExtra("filter", viewModel.filter)
            }, RequestCodes.FILTER_ACTIVITY)
        }

        return view
    }

    private fun bindViews(view: View) {
        nextFragment = view.find(R.id.ll_next_fragment)
        recyclerView = view.find(R.id.rv_discover)
        filter = view.find(R.id.fab_filter)
    }
}