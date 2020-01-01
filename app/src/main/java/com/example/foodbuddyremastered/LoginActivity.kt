package com.example.foodbuddyremastered

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.example.foodbuddyremastered.utils.NotifUtils
import com.rengwuxian.materialedittext.MaterialEditText
import org.jetbrains.anko.find

class LoginActivity : AppCompatActivity() {

    private lateinit var email: MaterialEditText
    private lateinit var password: MaterialEditText
    private lateinit var rememberMe: CheckBox
    private lateinit var forgotPass: TextView
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindViews()

        val notifUtils = NotifUtils(this)

        login.setOnClickListener {
            val emailVal = email.text.toString()
            val passVal = password.text.toString()

            if (emailVal.isEmpty() || passVal.isEmpty()) {
                notifUtils.createToast("One or more fields are empty").show()
            } else {
                // TODO: auth user
            }
        }

        forgotPass.setOnClickListener {

        }
    }

    private fun bindViews() {
        email = find(R.id.met_email)
        password = find(R.id.met_password)
        rememberMe = find(R.id.cb_remember_me)
        forgotPass = find(R.id.tv_forgot_password)
        login = find(R.id.btn_log_in)
    }
}
