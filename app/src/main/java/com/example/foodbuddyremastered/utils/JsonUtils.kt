package com.example.foodbuddyremastered.utils

import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import com.google.gson.Gson
import com.loopj.android.http.RequestParams
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

class JsonUtils {
    companion object {
        fun userToReqParams(user: User): RequestParams {
            return RequestParams().apply {
                put("email", user.email)
                put("password", user.password)
                put("firstName", user.firstName)
                put("lastName", user.lastName)
                put("city", user.city)
                put("country", user.country)
                put("phoneNumber", user.phoneNumber)
                put("hasPhoto", user.hasPhoto)
                put("profileComplete", user.profileComplete)
                put("gender", user.gender)
                put("age", user.age)
                put("eatTimes", Gson().toJson(user.eatTimes))
                put("partnerMinAge", user.partnerMinAge)
                put("partnerMaxAge", user.partnerMaxAge)
                put("partnerGender", user.partnerGender)
                put("encodedImageData", user.compressedImage.encodedValue)
                put("imageName", user.compressedImage.imageName)
            }
        }

        fun jsonObjectToUser(json: JSONObject): User {
            return User().also {
                it.id = json.getString("_id")
                it.email = json.getString("email")
                it.firstName = json.getString("firstName")
                it.lastName = json.getString("lastName")
                it.city = json.getString("city")
                it.country = json.getString("country")
                it.phoneNumber = json.getString("phoneNumber")
                it.hasPhoto = json.getBoolean("hasPhoto")
                it.profileComplete = json.getBoolean("profileComplete")
                it.gender = json.getString("gender")
                it.age = json.getInt("age")
                it.partnerMinAge = json.getInt("partnerMinAge")
                it.partnerMaxAge = json.getInt("partnerMaxAge")
                it.partnerGender = json.getString("partnerGender")

                it.eatTimes.addAll(ArrayList<EatTimes>().apply {
                    val array = json.getJSONArray("eatTimes")
                    for (index in 0 until array.length()) {
                        add(EatTimes().also { eatTimes ->
                            eatTimes.start = array.getJSONObject(index).getString("start")
                            eatTimes.end = array.getJSONObject(index).getString("end")
                        })
                    }
                })
            }
        }

        fun discoverFilterToParams(filter: UserFilter): RequestParams {
            return RequestParams().apply {
                put("start", filter.start)
                put("end", filter.end)
                put("city", filter.city)
                put("country", filter.country)
                put("gender", filter.gender)
                put("minAge", filter.minAge)
                put("maxAge", filter.maxAge)
                put("zodiacSigns", StringBuilder().apply {
                    for (index in filter.zodiacSigns.indices) {
                        append(filter.zodiacSigns[index].name)
                        if (index != filter.zodiacSigns.size - 1) {
                            append("_")
                        }
                    }

                    toString()
                })
            }
        }

        fun jsonArrayToUserArray(jsonArray: JSONArray): ArrayList<User> {
            return ArrayList<User>().also {
                for (index in 0 until jsonArray.length()) {
                    it.add(User().also { user ->
                        val obj = jsonArray.getJSONObject(index)
                        val eatTimesJson = obj.getJSONArray("eatTimes")

                        // eat times loop
                        for (i in 0 until eatTimesJson.length()) {
                            user.eatTimes.add(EatTimes().also { eatTimes ->
                                eatTimes.start = eatTimesJson.getJSONObject(i).getString("start")
                                eatTimes.end = eatTimesJson.getJSONObject(i).getString("end")
                            })
                        }

                        user.id = obj.getString("_id")
                        // also add photoId after implementing photo upload
                        user.email = obj.getString("email")
                        user.firstName = obj.getString("firstName")
                        user.lastName = obj.getString("lastName")
                        user.city = obj.getString("city")
                        user.country = obj.getString("country")
                        user.phoneNumber = obj.getString("phoneNumber")
                        user.hasPhoto = obj.getBoolean("hasPhoto")
                        user.profileComplete = obj.getBoolean("profileComplete")
                        user.gender = obj.getString("gender")
                        user.age = obj.getInt("age")
                        user.partnerGender = obj.getString("partnerGender")
                        user.partnerMaxAge = obj.getInt("partnerMaxAge")
                        user.partnerMinAge = obj.getInt("partnerMinAge")
                    })
                }
            }
        }

        fun textMessageToJson(message: Message): JSONObject {
            return JSONObject().apply {
                with(message) {
                    put("senderName", senderName)
                    put("senderId", senderId)
                    put("receiverId", receiverId)
                    put("timeSent", timeSent)
                    put("message", this.message)
                    put("type", type)
                }
            }
        }

        fun jsonToMessage(map: Map<String, String>): Message {

            return when (val type = map.getValue("type").toInt()) {
                Message.TEXT_MESSAGE -> {
                    Message().apply {
                        this.type = type
                        senderName = map.getValue("senderName")
                        senderId = map.getValue("senderId")
                        receiverId = map.getValue("receiverId")
                        timeSent = map.getValue("timeSent")
                        message = map.getValue("message")
                    }
                }

                else -> Message()
            }
        }
    }
}