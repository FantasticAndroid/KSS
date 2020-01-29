package com.android.betterstetho.retro

import com.android.betterstetho.Constant
import com.android.stethodebug.RetroUtil
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

class ApiClient(private val baseUrl:String) {

    private fun getOkHttpClient(): OkHttpClient {

        val okHttpBuilder =  RetroUtil.getOkHttpBuilder()

        okHttpBuilder.addInterceptor { chain ->
                val request = chain.request()
                val requestUrl = URLDecoder.decode(request.url().toString(), "UTF-8")
                val builder = request.newBuilder()
                builder.addHeader(
                    Constant.HeaderKeys.KEY_ACCEPT,
                    Constant.HeaderValues.VALUE_ACCEPT
                )
                builder.addHeader(
                    Constant.HeaderKeys.KEY_CHANNEL,
                    Constant.HeaderValues.VALUE_CHANNEL
                )
                builder.addHeader(
                    Constant.HeaderKeys.KEY_CONTENT_TYPE,
                    Constant.HeaderValues.VALUE_CONTENT_TYPE
                )

                builder.method(request.method(), request.body())
                builder.url(requestUrl)

                chain.proceed(builder.build())
            }
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
        val okHttpClient = okHttpBuilder.build()
        okHttpClient.dispatcher().maxRequests = 8
        return okHttpClient
    }

    fun getClient(): Retrofit {

        val okHttpClient = getOkHttpClient()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

}