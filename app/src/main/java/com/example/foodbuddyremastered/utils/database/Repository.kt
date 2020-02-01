package com.example.foodbuddyremastered.utils.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.foodbuddyremastered.models.LocalUser
import com.example.foodbuddyremastered.models.Message
import com.example.foodbuddyremastered.models.User
import com.example.foodbuddyremastered.models.UserFilter
import io.reactivex.Flowable

class Repository(context: Context) {

    val db = DatabaseSingleton.getInstance(context)

    fun insertUser(localUser: LocalUser) {
        db.userDao().insertLocalUser(localUser)
    }

    fun getLocalUsers(): List<LocalUser> {
        return db.userDao().getUsers()
    }

    fun findUser(email: String): List<LocalUser> {
        return db.userDao().findUser(email)
    }

    fun updateLocalUser(localUser: LocalUser) {
        db.userDao().updateLocalUser(localUser)
    }

    fun getAuthenticatedUser(b: Boolean): List<LocalUser> {
        return db.userDao().getAuthenticatedUser(b)
    }

    fun getAuthenticatedUserLive(b: Boolean): Flowable<List<LocalUser>> {
        return db.userDao().getAuthenticatedUserLive(b)
    }

    fun nukeLocalUsers() {
        db.userDao().nuke()
    }

    fun getMessagesForConversationLive(conversationId: String, ownerId: String): LiveData<List<Message>> {
        return db.messageDao().getMessagesForConversationLive(conversationId, ownerId)
    }

    fun getMessagesForConversation(conversationId: String, ownerId: String): List<Message> {
        return db.messageDao().getMessagesForConversation(conversationId, ownerId)
    }

    fun getLastMessageForConversation(conversationId: String, ownerId: String): Message {
        return db.messageDao().getLastMessageForConversation(conversationId, ownerId)
    }

    fun getLastMessageLive(ownerId: String): LiveData<Message> {
        return db.messageDao().getLastMessageLive(ownerId)
    }

    fun insertMessage(message: Message) {
        db.messageDao().insertMessage(message)
    }

    fun getConversationIds(ownerId: String): List<String> {
        return db.messageDao().getConversationIds(ownerId)
    }

    fun nukeMessages() {
        db.messageDao().nuke()
    }

    fun insertFilter(filter: UserFilter) {
        db.filterDao().insertFilter(filter)
    }

    fun getActiveFilter(ownerId: String, b: Boolean): List<UserFilter> {
        return db.filterDao().getActiveFilter(b, ownerId)
    }

    fun getActiveFilterLive(ownerId: String, b: Boolean): LiveData<List<UserFilter>> {
        return db.filterDao().getActiveFilterLive(b, ownerId)
    }

    fun getAllFilters(ownerId: String): LiveData<List<UserFilter>> {
        return db.filterDao().getAllFilters(ownerId)
    }

    fun getFilterNamesLive(ownerId: String): LiveData<List<String>> {
        return db.filterDao().getFilterNamesLive(ownerId)
    }

    fun updateFilter(filter: UserFilter) {
        db.filterDao().updateFilter(filter)
    }

    fun nukeFilters(ownerId: String) {
        db.filterDao().nukeTable(ownerId)
    }
}