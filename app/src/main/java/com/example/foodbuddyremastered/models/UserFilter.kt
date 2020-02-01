package com.example.foodbuddyremastered.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Filter")
class UserFilter: Serializable {

    companion object {
        const val DEFAULT_FILTER = 1
        const val CUSTOM_FILTER = 2
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    lateinit var name: String

    lateinit var start: String
    lateinit var end: String
    lateinit var city: String
    lateinit var country: String
    var minAge: Int = 18
    var maxAge: Int = 18
    lateinit var gender: String
    var tolerance: Int = 0
    var zodiacSigns = ArrayList<ZodiacSign>()
    var type = -1
    var isLastUsed = false
    lateinit var ownerId: String


}