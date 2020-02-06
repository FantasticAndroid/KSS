package com.sample.android.network

import com.sample.android.model.ProductListModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    // This method is used to get product list from server
    @GET("json/")
    fun getProductListFromServer(): Call<ProductListModel?>?
}