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
import com.example.foodbuddyremastered.events.SocketEvents
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.HttpStatus
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class APIClient {

    lateinit var socket: Socket

    companion object {
        const val TAG = "APIClient"
    }

    fun initSocket() {
        socket = IO.socket(ApiUrls.SOCKET_URL)
        socket.connect()
    }

    fun disconnectSocket() {
        socket.disconnect()
    }

    private val client = AsyncHttpClient()

    fun registerUserEmail(user: User, context: Context) {
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

    fun authUserEmail(email: String, password: String, res: MutableLiveData<ResponseEvent>) {
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

                res.postValue(ResponseEvent().also {
                    it.action = Actions.AUTH_USER_EMAIL_RESPONSE
                    it.status = status
                    it.payload = user
                })

                Log.d(TAG, "authUserEmail: Response -> $response")
            }
        })
    }

    fun discoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>, user: User) {
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

    fun emitTextMessage(message: Message) {
        socket.emit(SocketEvents.MESSAGE, JsonUtils.textMessageToJson(message))
    }

}