package com.sample.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RankingProductModel {
    @SerializedName("id")
    @Expose
    var id: Int? = 0
    @SerializedName("view_count")
    @Expose
    var viewCount: Int? = 0
    @SerializedName("order_count")
    @Expose
    var orderCount: Int? = 0
    @SerializedName("shares")
    @Expose
    var shareCount: Int? = 0
}