package com.example.foodbuddyremastered.utils.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodbuddyremastered.models.UserFilter

@Dao
interface UserFilterDao {

    @Insert
    fun insertFilter(filter: UserFilter)

    @Query("Select * From Filter Where ownerId = :ownerId and isLastUsed = :b")
    fun getActiveFilter(b: Boolean, ownerId: String): List<UserFilter>

    @Query("Select * From Filter Where ownerId = :ownerId and isLastUsed = :b")
    fun getActiveFilterLive(b: Boolean, ownerId: String): LiveData<List<UserFilter>>

    @Query("Select * From Filter Where ownerId = :ownerId")
    fun getAllFilters(ownerId: String): LiveData<List<UserFilter>>

    @Update
    fun updateFilter(filter: UserFilter)

    @Query("Select name From Filter Where ownerId = :ownerId")
    fun getFilterNamesLive(ownerId: String): LiveData<List<String>>

    @Query("Delete From Filter Where id = :id")
    fun removeFilter(id: Int)

    @Query("Delete From Filter where ownerId = :ownerId")
    fun nukeTable(ownerId: String)
}
