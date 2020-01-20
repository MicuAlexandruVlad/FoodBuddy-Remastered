package com.example.foodbuddyremastered.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import com.example.foodbuddyremastered.R
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.utils.database.Repository
import com.example.foodbuddyremastered.views.dialogs.SelectUserDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class SplashActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_splash)

        bindViews()

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
            Repository(this@SplashActivity).also {
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
