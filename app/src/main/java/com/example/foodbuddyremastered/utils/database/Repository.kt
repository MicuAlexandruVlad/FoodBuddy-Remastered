package com.example.foodbuddyremastered.utils.database

import android.content.Context
import androidx.room.Room
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.User

class Repository(context: Context) {

    val db = Room.databaseBuilder(
        context,
        Database::class.java, "test2"
    ).build()

    fun insertUser(localUser: LocalUser) {
        db.userDao().insertLocalUser(localUser)
    }

    fun getLocalUsers(): List<LocalUser> {
        return db.userDao().getUsers()
    }

    fun findUser(email: String): List<LocalUser> {
        return db.userDao().findUser(email)
    }

    fun nukeLocalUsers() {
        db.userDao().nuke()
    }
}