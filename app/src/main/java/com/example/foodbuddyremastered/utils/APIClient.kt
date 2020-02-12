package com.example.foodbuddyremastered.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodbuddyremastered.constants.Actions
import com.example.foodbuddyremastered.constants.ApiUrls
import com.example.foodbuddyremastered.events.ResponseEvent
import com.example.foodbuddyremastered.events.SocketEvents
import com.example.foodbuddyremastered.models.*
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.HttpStatus
import org.json.JSONObject
import java.lang.StringBuilder

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

    fun authUserEmail(email: String, password: String, res: MutableLiveData<ResponseEvent>,
                      client: AsyncHttpClient = AsyncHttpClient()) {
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

    fun discoverUsers(filter: UserFilter, list: MutableLiveData<List<User>>, user: User, client: AsyncHttpClient) {
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

    fun getConversationUsers(ids: List<String>, list: MutableLiveData<ResponseEvent>, client: AsyncHttpClient = AsyncHttpClient()) {

        val builder = StringBuilder()
        val params = RequestParams().apply {
            for (index in ids.indices) {
                builder.append(ids[index])

                if (index < ids.size - 1) {
                    builder.append("_")
                }
            }

            put("ids", builder.toString())
        }

        client.get(ApiUrls.GET_CONVERSATION_USERS, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")

                list.postValue(ResponseEvent().apply {
                    this.status = status
                    payload = JsonUtils.jsonArrayToUserArray(response.getJSONArray("data"))
                })
            }
        })

    }

    fun registerPlace(place: Place, liveResponse: MutableLiveData<ResponseEvent>,
                      client: AsyncHttpClient = AsyncHttpClient()) {

        client.post(ApiUrls.REGISTER_PLACE, JsonUtils.placeToRequestParams(place),
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    super.onSuccess(statusCode, headers, response)

                    val status = response!!.getInt("status")
                    val id = response.getString("id")

                    Log.d(TAG, "registerPlace: id -> $id")
                    Log.d(TAG, "registerPlace: status -> $status")

                    liveResponse.postValue(ResponseEvent().apply {
                        payload = id
                        this.status = status
                        action = Actions.PLACE_REGISTERED
                    })
                }
            })
    }

    fun uploadPlaceImage(compressedImage: CompressedImage, liveResponse: MutableLiveData<ResponseEvent>,
                         id: String, index: Int, client: AsyncHttpClient = AsyncHttpClient()
    ) {

        val params = JsonUtils.compressedImageToRequestParams(compressedImage, id)

        client.post(ApiUrls.UPLOAD_PLACE_IMAGE, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    super.onSuccess(statusCode, headers, response)

                    liveResponse.postValue(ResponseEvent().apply {
                        status = response!!.getInt("status")
                        action = Actions.PLACE_IMAGE_UPLOADED
                        payload = index
                        payload2 = response.getString("id")
                    })
                }
            })
    }

    fun requestPlaces(
        city: String, country: String, limit: Int, liveResponse: MutableLiveData<ResponseEvent>,
        client: AsyncHttpClient = AsyncHttpClient()
    ) {
        val params = RequestParams().also {
            it.put("city", city)
            it.put("country", country)
            it.put("limit", limit)
        }

        client.get(ApiUrls.REQUEST_PLACES, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")
                val places = response.getJSONArray("places")

                liveResponse.postValue(ResponseEvent().apply {
                    this.status = status
                    action = Actions.RECEIVED_PLACES
                    payload = places
                })
            }
        })
    }

    fun registerReview(review: Review, liveResponse: MutableLiveData<ResponseEvent>,
                       client: AsyncHttpClient = AsyncHttpClient()) {
        val params = JsonUtils.reviewToRequestParams(review)

        client.post(ApiUrls.UPLOAD_REVIEW, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                val status = response!!.getInt("status")

                liveResponse.postValue(ResponseEvent().apply {
                    action = Actions.REVIEW_UPLOADED
                    this.status = status
                })
            }
        })
    }

    fun getReviews(
        placeId: String, limit: Int,
        liveResponse: MutableLiveData<ResponseEvent>,
        client: AsyncHttpClient = AsyncHttpClient()
    ) {
        val params = RequestParams().apply {
            put("placeId", placeId)
            put("limit", limit)
        }

        client.get(ApiUrls.GET_REVIEWS, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                Log.d(TAG, "getReviews: response -> $response")

                val status = response!!.getInt("status")

                liveResponse.postValue(ResponseEvent().apply {
                    action = Actions.RECEIVED_REVIEWS
                    payload = response.getJSONArray("reviews")
                    this.status = status
                })
            }
        })
    }

    fun emitTextMessage(message: Message) {
        socket.emit(SocketEvents.MESSAGE, JsonUtils.textMessageToJson(message))
    }

}