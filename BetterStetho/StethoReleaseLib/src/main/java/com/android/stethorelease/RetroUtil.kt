package com.android.stethorelease

import okhttp3.OkHttpClient

class RetroUtil {

    companion object {

        fun getOkHttpBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }
    }
}