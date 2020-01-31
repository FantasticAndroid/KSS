package com.android.betterstetho.retro

import com.android.betterstetho.BaseModel
import com.android.betterstetho.Constant
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET(Constant.METHOD_URL)
    fun getRetroResponse() : Call<BaseModel>
}