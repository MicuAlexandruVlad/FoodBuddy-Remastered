package com.example.foodbuddyremastered.views.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.adapters.LocalUserAdapter
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.views.MainActivity
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class SelectUserDialog(context: Context, private val owner: LifecycleOwner): Dialog(context) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cancel: Button
    private lateinit var done: Button

    var isDone = false
    private lateinit var adapter: LocalUserAdapter
    private lateinit var localUsers: ArrayList<LocalUser>
    lateinit var selectedLocalUser: LocalUser
    private val localUserClick = MutableLiveData<Int>()
    private val notifUtils = NotifUtils(context)
    private var canAuth = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_select_user)

        bindViews()

        localUsers = ArrayList()
        adapter = LocalUserAdapter(localUsers, owner, localUserClick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        localUserClick.observe(owner, Observer<Int> { index ->
            val response = MutableLiveData<ResponseEvent>()
            if (canAuth) {
                canAuth = false
                APIClient().apply {
                    val selectedUser = localUsers[index]
                    doAsync {
                        Repository(context).apply {
                            // set this user as being currently authenticated within the app
                            selectedUser.isAuthenticated = true
                            updateLocalUser(selectedUser)
                        }
                    }
                    authUserEmail(selectedUser.email, selectedUser.password, response)
                }
            }

            response.observe(owner, Observer<ResponseEvent> { event ->
                if (event.action == Actions.AUTH_USER_EMAIL_RESPONSE) {
                    when (event.status) {
                        HttpStatus.SC_OK -> {
                            val user = event.payload as User

                            (owner as Context).startActivity(Intent((owner as Context)
                                , MainActivity::class.java).apply {
                                putExtra("currentUser", user)
                            })

                            canAuth = true
                        }

                        else -> {
                            notifUtils.createToast("User not found").show()
                        }
                    }
                }
            })
        })

        cancel.setOnClickListener {
            dismiss()
        }

        done.setOnClickListener {
            isDone = true
            dismiss()
        }
    }

    private fun bindViews() {
        recyclerView = find(R.id.rv_accounts)
        cancel = find(R.id.btn_cancel)
        done = find(R.id.btn_done)
    }

    fun setLocalUsers(list: ArrayList<LocalUser>) {
        localUsers.clear()
        localUsers.addAll(list)
        adapter.notifyDataSetChanged()
    }
}