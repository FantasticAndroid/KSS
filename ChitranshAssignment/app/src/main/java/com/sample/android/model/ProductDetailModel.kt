package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductDetailModel {
    @SerializedName("id")
    @Expose
    private val id: Int? = null
    @SerializedName("view_count")
    @Expose
    private val viewCount: Int? = null
    @SerializedName("order_count")
    @Expose
    private val orderCount: Int? = null
    @SerializedName("shares")
    @Expose
    private val shares: Int? = null
}