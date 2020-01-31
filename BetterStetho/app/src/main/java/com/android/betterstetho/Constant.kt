package com.android.betterstetho

class Constant {

    companion object{
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val METHOD_URL = "todos/1"
    }

    interface HeaderKeys {
        companion object {
            const val KEY_CHANNEL = "Channel"
            const val KEY_ACCEPT = "Accept"
            const val KEY_CONTENT_TYPE = "Content-Type"
        }
    }

    interface HeaderValues {
        companion object {
            const val VALUE_CHANNEL = "MOBILE"
            const val VALUE_ACCEPT = "application/json"
            const val VALUE_CONTENT_TYPE = "application/json"
        }
    }
}