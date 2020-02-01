package com.example.foodbuddyremastered.adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.foodbuddyremastered.views.fragments.ConversationsFragment
import com.example.foodbuddyremastered.views.fragments.DiscoverPeopleFragment

class ViewPagerAdapter(private val fragmentManager: FragmentManager,
                       private val conversationsBundle: Bundle,
                       private val discoverPeopleBundle: Bundle,
                       private val context: Context):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val items = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ConversationsFragment(context).apply { arguments = conversationsBundle }
            }
            else -> {
                DiscoverPeopleFragment(context).apply { arguments = discoverPeopleBundle }
            }
        }
    }

    override fun getCount(): Int {
        return items
    }
}