package com.example.foodbuddyremastered.utils

import android.util.Log
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.constants.ObjectTypes
import com.example.foodbuddyremastered.events.ObjectUploadedEvent
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.models.User
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

    override fun registerUserEmail(user: User) {
        val params = JsonUtils.userToReqParams(user)

        client.post(ApiUrls.REGISTER_USER_EMAIL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")
                val objectUploadedEvent = ObjectUploadedEvent()
                objectUploadedEvent.objType = ObjectTypes.USER
                objectUploadedEvent.status = status

                emitObjectUploadedEvent(objectUploadedEvent)
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

    private fun emitObjectUploadedEvent(objectUploadedEvent: ObjectUploadedEvent) {
        EventBus.getDefault().post(objectUploadedEvent)
    }

    private fun emitResponseEvent(responseEvent: ResponseEvent) {
        EventBus.getDefault().post(responseEvent)
    }

}