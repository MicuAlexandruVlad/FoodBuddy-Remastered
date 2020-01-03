package com.example.foodbuddyremastered.utils

import com.example.foodbuddyremastered.models.EatTimes
import com.example.foodbuddyremastered.models.User
import com.google.gson.Gson
import com.loopj.android.http.RequestParams
import org.json.JSONObject

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
            }
        }

        fun jsonObjectToUser(json: JSONObject): User {
            return User().also {
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
    }
}