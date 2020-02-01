package com.example.foodbuddyremastered.constants

class ApiUrls {
    companion object {
        const val BASE_URL: String = "http://192.168.0.19:3000/"
        private const val USER_PATH: String = "users/"
        const val SOCKET_URL = "http://192.168.0.19:3000"

        const val REGISTER_USER_EMAIL = BASE_URL + USER_PATH + "register-user-email/"
        const val AUTH_USER_EMAIL = BASE_URL + USER_PATH + "auth-user-email/"

        const val DISCOVER_USERS = BASE_URL + USER_PATH + "discover-users/"

        const val GET_CONVERSATION_USERS = BASE_URL + USER_PATH + "conversation-user-data/"


    }
}