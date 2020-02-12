package com.example.foodbuddyremastered.constants

class ApiUrls {
    companion object {
        private const val BASE_URL: String = "http://192.168.0.19:3000/"
        private const val USER_PATH: String = "users/"
        private const val PLACE_PATH = "places/"
        private const val PLACE_IMAGE_PATH = "place-images/"
        private const val USER_IMAGE_PATH = "user-images/"
        private const val REVIEW_PATH = "reviews/"
        const val SOCKET_URL = "http://192.168.0.19:3000"

        const val REGISTER_USER_EMAIL = BASE_URL + USER_PATH + "register-user-email/"
        const val AUTH_USER_EMAIL = BASE_URL + USER_PATH + "auth-user-email/"

        const val DISCOVER_USERS = BASE_URL + USER_PATH + "discover-users/"

        const val GET_CONVERSATION_USERS = BASE_URL + USER_PATH + "conversation-user-data/"

        const val REGISTER_PLACE = BASE_URL + PLACE_PATH + "register-place/"
        const val REQUEST_PLACES = BASE_URL + PLACE_PATH + "places/"
        const val UPLOAD_PLACE_IMAGE = BASE_URL + PLACE_IMAGE_PATH + "upload-image/"

        const val UPLOAD_REVIEW = BASE_URL + REVIEW_PATH + "register-review/"

        const val GET_REVIEWS = BASE_URL + REVIEW_PATH + "reviews/"

        fun getUserPhotoSmall(userId: String, photoId: String): String {
            return BASE_URL + USER_IMAGE_PATH + "photo-small/$userId/images/$photoId/"
        }

        fun getUserPhotoNormal(userId: String, photoId: String): String {
            return BASE_URL + USER_IMAGE_PATH + "photo-normal/$userId/images/$photoId/"
        }

        fun getUserProfilePhotoSmall(userId: String): String {
            return BASE_URL + USER_IMAGE_PATH + "profile-normal/$userId/"
        }

        fun getUserProfilePhotoNormal(userId: String): String {
            return BASE_URL + USER_IMAGE_PATH + "profile-normal/$userId/"
        }

        fun getPlacePhoto(placeId: String, photoId: String): String {
            return BASE_URL + PLACE_IMAGE_PATH + "place/$placeId/images/$photoId/"
        }
    }
}