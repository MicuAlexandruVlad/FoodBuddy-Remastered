package com.example.foodbuddyremastered.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.constants.ObjectTypes
import com.example.foodbuddyremastered.events.ObjectUploadedEvent
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.HttpStatus
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class APIClient: ApiInterface {

    companion object {
        const val TAG = "APIClient"
    }

    private val client = AsyncHttpClient()

    override fun registerUserEmail(user: User, context: Context) {
        val params = JsonUtils.userToReqParams(user)

        client.post(ApiUrls.REGISTER_USER_EMAIL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")

                if (status == HttpStatus.SC_CREATED) {
                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()

                    ((context) as Activity).finish()
                }
            }
        })
    }

    override fun authUserEmail(email: String, password: String) {
        val params = RequestParams().apply {
            put("email", email)
            put("password", password)
        }

        client.get(ApiUrls.AUTH_USER_EMAIL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")
                val user = if (status == HttpStatus.SC_OK) {
                    JsonUtils.jsonObjectToUser(response.getJSONObject("user"))
                } else {
                    User()
                }

                Log.d(TAG, "authUserEmail: Response -> $response")

                emitResponseEvent(ResponseEvent().also {
                    it.action = Actions.AUTH_USER_EMAIL_RESPONSE
                    it.status = status
                    it.payload = user
                })
            }
        })
    }

    override fun discoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>, user: User) {
        val params = JsonUtils.discoverFilterToParams(filter)
        params.put("userEmail", user.email)
        params.put("userAge", user.age)
        params.put("userGender", user.gender)

        Log.d(TAG, "discoverUsers: Params -> ${ Gson().toJson(params) }")

        client.get(ApiUrls.DISCOVER_USERS, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")

                Log.d(TAG, "discoverUsers: Response -> $response")

                list.postValue(ArrayList<User>().apply {
                    addAll(JsonUtils.jsonArrayToUserArray(response.getJSONArray("data")))
                })
            }
        })
    }

    private fun emitObjectUploadedEvent(objectUploadedEvent: ObjectUploadedEvent) {
        EventBus.getDefault().post(objectUploadedEvent)
    }

    private fun emitResponseEvent(responseEvent: ResponseEvent) {
        EventBus.getDefault().post(responseEvent)
    }

}