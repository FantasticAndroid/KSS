package com.sample.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductListModel {
    @SerializedName("categories")
    @Expose
    val categoryList: List<CategoryModel>? = null
    @SerializedName("rankings")
    @Expose
    val rankingList: List<RankingModel>? = null
}