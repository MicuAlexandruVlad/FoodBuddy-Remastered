package com.example.foodbuddyremastered.utils

import com.example.foodbuddyremastered.models.*
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
                        conversationId = senderId
                    }
                }

                else -> Message()
            }
        }

        fun jsonArrayToZodiacSignsArray(array: JSONArray): ArrayList<ZodiacSign> {
            return ArrayList<ZodiacSign>().apply {
                for (index in 0 until array.length()) {
                    val obj = array.getJSONObject(index)
                    add(ZodiacSign().apply {
                        this.action = obj.getInt("action")
                        this.selected = obj.getBoolean("selected")
                        this.name = obj.getString("name")
                    })
                }
            }
        }

        fun placeToRequestParams(place: Place): RequestParams {
            return RequestParams().apply {
                with(place) {
                    put("name", name)
                    put("city", address.city)
                    put("country", address.country)
                    put("streetName", address.street)
                    put("zip", address.postalCode)
                    put("description", description)
                    put("type", placeType)
                    put("hasSchedule", hasSchedule)
                    put("scheduleMonday", schedule.getValue("Monday"))
                    put("scheduleTuesday", schedule.getValue("Tuesday"))
                    put("scheduleWednesday", schedule.getValue("Wednesday"))
                    put("scheduleThursday", schedule.getValue("Thursday"))
                    put("scheduleFriday", schedule.getValue("Friday"))
                    put("scheduleSaturday", schedule.getValue("Saturday"))
                    put("scheduleSunday", schedule.getValue("Sunday"))
                    put("rating", rating)
                    put("visitors", visitors)
                    put("createdBy", createdBy)
                    put("lastEditedBy", lastEditedBy)
                }
            }
        }

        fun compressedImageToRequestParams(compressedImage: CompressedImage, id: String): RequestParams {
            return RequestParams().apply {
                put("data", compressedImage.encodedValue)
                put("name", compressedImage.imageName)
                put("signature", compressedImage.signature)
                put("placeId", id)
            }
        }

        fun jsonObjectToPlace(jsonObject: JSONObject): Place {
            return Place().apply {
                with(jsonObject) {
                    id = getString("_id")
                    name = getString("name")
                    address = Address().apply {
                        city = getString("city")
                        country = getString("country")
                        street = getString("street")
                        postalCode = getString("zip")
                    }
                    description = getString("description")
                    placeType = getString("type")
                    hasSchedule = getBoolean("hasSchedule")
                    rating = getDouble("rating")
                    visitors = getInt("visitors")
                    createdBy = getString("createdBy")
                    lastEditedBy = getString("lastEditedBy")
                    numReviews = getInt("reviews")
                    schedule = HashMap<String, String>().apply {
                        val s = getJSONArray("schedule")

                        put("Monday", s.getString(0))
                        put("Tuesday", s.getString(1))
                        put("Wednesday", s.getString(2))
                        put("Thursday", s.getString(3))
                        put("Friday", s.getString(4))
                        put("Saturday", s.getString(5))
                        put("Sunday", s.getString(6))
                    }
                    photoId = ArrayList<String>().apply {
                        val photoIds = getJSONArray("photos")
                        for (index in 0 until photoIds.length()) {
                            add(photoIds.getJSONObject(index).getString("_id"))
                        }
                    }
                }
            }
        }

        fun reviewToRequestParams(review: Review): RequestParams {
            return RequestParams().apply {
                with(review) {
                    put("content", content)
                    put("rating", rating)
                    put("userId", userId)
                    put("userName", userName)
                    put("placeId", placeId)
                    put("timestamp", timestamp)
                }
            }
        }

        fun jsonObjectToReview(jsonObject: JSONObject): Review {
            return Review().apply {
                content = jsonObject.getString("content")
                rating = jsonObject.getDouble("rating")
                userName = jsonObject.getString("userName")
                userId = jsonObject.getString("userId")
                timestamp = jsonObject.getString("timestamp")
            }
        }
    }
}