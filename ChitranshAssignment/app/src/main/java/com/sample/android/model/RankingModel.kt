package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class RankingModel {
    @SerializedName("ranking")
    @Expose
    val ranking: String? = null
    @SerializedName("products")
    @Expose
    val productDetailList: List<RankingProductModel>? = null
}