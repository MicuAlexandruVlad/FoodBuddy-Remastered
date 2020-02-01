package com.example.foodbuddyremastered.utils.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodbuddyremastered.models.LocalUser
import io.reactivex.Flowable

@Dao
interface LocalUserDao {

    @Insert
    fun insertLocalUser(localUser: LocalUser)

    @Query("Select * From LocalUser")
    fun getUsers(): List<LocalUser>

    @Query("Select * From LocalUser Where email = :email")
    fun findUser(email: String): List<LocalUser>

    @Query("Select * From LocalUser Where isAuthenticated = :isAuthenticated")
    fun getAuthenticatedUser(isAuthenticated: Boolean): List<LocalUser>

    @Query("Select * From LocalUser Where isAuthenticated = :isAuthenticated")
    fun getAuthenticatedUserLive(isAuthenticated: Boolean): Flowable<List<LocalUser>>

    @Delete
    fun removeUser(localUser: LocalUser)

    @Update
    fun updateLocalUser(localUser: LocalUser)

    @Query("Delete From LocalUser")
    fun nuke()
}