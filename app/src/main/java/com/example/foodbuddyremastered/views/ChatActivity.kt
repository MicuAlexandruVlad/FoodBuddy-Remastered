package com.example.foodbuddyremastered.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.databinding.ActivityChatBinding
import com.example.foodbuddyremastered.viewmodels.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val binding: ActivityChatBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewModel = ChatViewModel(application, this, this)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.initView()
    }
}
