package com.android.stethodebug

import android.content.Context
import android.util.Log
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class RetroUtil {

    companion object {

        fun initStethoAccordingly(context:Context) {
            Stetho.initializeWithDefaults(context)
        }

        fun getOkHttpBuilder(): OkHttpClient.Builder {
            val stethoInterceptor = StethoInterceptor()
            val okHttpBuilder = OkHttpClient.Builder()
            okHttpBuilder.addNetworkInterceptor(stethoInterceptor)
            Log.d("RetroUtil", "StethoInterceptor Add")
            return okHttpBuilder
        }
    }
}