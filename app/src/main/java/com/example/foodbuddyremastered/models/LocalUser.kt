package com.example.foodbuddyremastered.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocalUser")
class LocalUser {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var email = ""
    var password = ""
}
