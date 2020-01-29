package com.android.stethodebug

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class RetroUtil {

    companion object {

        fun getOkHttpBuilder(): OkHttpClient.Builder {
            val stethoInterceptor = StethoInterceptor()
            val okHttpBuilder = OkHttpClient.Builder()
            okHttpBuilder.addNetworkInterceptor(stethoInterceptor)
            return okHttpBuilder
        }
    }
}