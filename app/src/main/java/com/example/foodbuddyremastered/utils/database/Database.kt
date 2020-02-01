package com.example.foodbuddyremastered.utils.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodbuddyremastered.models.Conversation
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.UserFilter
import com.example.foodbuddyremastered.utils.Converters
import com.example.foodbuddyremastered.utils.database.dao.LocalUserDao
import com.example.foodbuddyremastered.utils.database.dao.MessageDao
import com.example.foodbuddyremastered.utils.database.dao.UserFilterDao
import javax.inject.Singleton

@Singleton
@Database(entities = [LocalUser::class, Message::class, UserFilter::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseSingleton: RoomDatabase() {
    abstract fun userDao(): LocalUserDao
    abstract fun messageDao(): MessageDao
    abstract fun filterDao(): UserFilterDao

    companion object{
        private var INSTANCE: DatabaseSingleton? = null
        fun getInstance(context: Context): DatabaseSingleton{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    DatabaseSingleton::class.java,
                    "roomdb8")
                    .build()
            }

            return INSTANCE as DatabaseSingleton
        }
    }
}