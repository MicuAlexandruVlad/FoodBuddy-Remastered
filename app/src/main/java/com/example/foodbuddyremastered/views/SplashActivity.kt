package com.example.foodbuddyremastered.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import com.example.foodbuddyremastered.R
import org.jetbrains.anko.find

class SplashActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var twitter: RelativeLayout
    private lateinit var google: RelativeLayout
    private lateinit var facebook: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        bindViews()

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
    }
}
