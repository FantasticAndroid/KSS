package com.android.stethorelease

import android.content.Context
import okhttp3.OkHttpClient

class RetroUtil {

    companion object {

        fun initStethoAccordingly(context:Context) {
            // Do not init Stetho in Release Build
            /*Stetho.initializeWithDefaults(context)*/
        }

        fun getOkHttpBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }
    }
}