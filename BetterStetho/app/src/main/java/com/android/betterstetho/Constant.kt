package com.android.betterstetho

class Constant {

    interface HeaderKeys {
        companion object {
            val KEY_AUTHORIZATION = "Authorization"
            val KEY_USER_AUTHORIZATION = "User_Authorization"
            val KEY_CHANNEL = "Channel"
            val KEY_DEVICE_TYPE = "Device-Type"
            val KEY_ACCEPT = "Accept"
            val KEY_APP_VERSION = "App-Version"
            val KEY_DB_ID = "DB-Id"
            val KEY_CONTENT_TYPE = "Content-Type"
            val KEY_CONNECTION = "Connection"
            val KEY_X_CSRF_TOKEN = "X-CSRF-Token"
            val KEY_UUID = "uuid"
            val KEY_DEVICE_ID = "device_id"
            val KEY_CHANNEL_NO = "Channel-No"
            val KEY_CITY = "city"
            val KEY_RASHI_NAME = "rashi_name"
            val KEY_GENDER = "gender"
            val KEY_DOB = "dob"
            val KEY_UPDATE_DATE = "Update-Date"
            val KEY_INSTALL_DATE = "Install-Date"
            val KEY_MOBILE_NUMBER = "mobile_no"
            val KEY_EVENT_REMINDER = "EventReminder"
            val KEY_CHANNEL_NAME = "ChannelName"
        }
    }


    interface HeaderValues {
        companion object {
            val VALUE_CHANNEL = "MOBILE"
            val VALUE_CHANNEL_NO = "521"
            val VALUE_DEVICE_TYPE = "ANDROID"
            val VALUE_ACCEPT = "application/json"
            val VALUE_CONTENT_TYPE = "application/json"
            val VALUE_CLOSE = "close"
        }
    }
}