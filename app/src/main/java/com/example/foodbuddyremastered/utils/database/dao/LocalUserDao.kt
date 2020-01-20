package com.example.foodbuddyremastered.utils.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodbuddyremastered.models.LocalUser

@Dao
interface LocalUserDao {

    @Insert
    fun insertLocalUser(localUser: LocalUser)

    @Query("Select * From LocalUser")
    fun getUsers(): List<LocalUser>

    @Query("Select * From LocalUser Where email = :email")
    fun findUser(email: String): List<LocalUser>

    @Delete
    fun removeUser(localUser: LocalUser)

    @Query("Delete From LocalUser")
    fun nuke()
}