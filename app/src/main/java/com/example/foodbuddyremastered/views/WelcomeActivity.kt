package com.example.foodbuddyremastered.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.utils.APIClient
import com.example.foodbuddyremastered.utils.NotifUtils
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.views.dialogs.SelectUserDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.HttpStatus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var twitter: RelativeLayout
    private lateinit var google: RelativeLayout
    private lateinit var facebook: RelativeLayout
    private lateinit var showAccounts: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        bindViews()

        val liveData = MutableLiveData<ResponseEvent>()

        liveData.observe(this@WelcomeActivity, Observer<ResponseEvent> { event ->
            when (event.status) {
                HttpStatus.SC_OK -> {
                    val currentUser = event.payload as User

                    startActivity(Intent(this@WelcomeActivity,
                        MainActivity::class.java).apply {
                        putExtra("currentUser", currentUser)
                    })
                }

                else -> {
                    NotifUtils(this@WelcomeActivity).createToast("User not found").show()
                }
            }
        })


        // check if there is any user already authenticated
        doAsync {
            Repository(this@WelcomeActivity).apply {
                val list = getAuthenticatedUser(true)

                Log.d(TAG, "Auth user -> ${ Gson().toJson(list) }")

                if (list.isNotEmpty() && list.size == 1) {
                    // there is one user authenticated -> then auth this user
                    val user = list[0]

                    APIClient().authUserEmail(user.email, user.password, liveData, SyncHttpClient())
                }
            }
        }

        showAccounts.setOnClickListener {
            val remember = readFromSharedPreferences(R.string.remember)

            Log.d(TAG, "Remember value -> $remember")

            if (remember) {
                val list = ArrayList<LocalUser>()
                getLocalUsers(list)

            }
        }

        // doAsync { Repository(this@SplashActivity).nukeLocalUsers() }

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun bindViews() {
        login = find(R.id.btn_log_in)
        signUp = find(R.id.btn_sign_up)
        twitter = find(R.id.rl_twitter)
        facebook = find(R.id.rl_facebook)
        google = find(R.id.rl_google)
        showAccounts = find(R.id.fab_local_users)
    }

    private fun displayLocalUsersDialog(list: ArrayList<LocalUser>) {
        val dialog = SelectUserDialog(this, this)

        dialog.create()
        dialog.setLocalUsers(list)

        dialog.show()
    }

    private fun readFromSharedPreferences(id: Int): Boolean {
        val pref = getSharedPreferences("SP", Context.MODE_PRIVATE)

        return with(pref) {
            getBoolean(getString(id), false)
        }
    }

    // TODO: Auto auth if there is only 1 local user
    private fun getLocalUsers(list: ArrayList<LocalUser>) {
        doAsync {
            Repository(this@WelcomeActivity).also {
                list.addAll(it.getLocalUsers())
                runOnUiThread {
                    displayLocalUsersDialog(list)
                }

                /*if (list.size > 1) {
                    runOnUiThread {
                        displayLocalUsersDialog(list)
                    }
                }*/
            }
        }
    }
}
