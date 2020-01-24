package com.example.foodbuddyremastered.utils.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.utils.database.dao.LocalUserDao
import com.example.foodbuddyremastered.utils.database.dao.MessageDao

@Database(entities = [LocalUser::class, Message::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): LocalUserDao
    abstract fun messageDao(): MessageDao

}