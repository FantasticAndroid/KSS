package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CategoryModel {

    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("products")
    @Expose
    val productListModel: List<ProductModel>? = null
    @SerializedName("child_categories")
    @Expose
    val childCategoryList: List<Int>? = null
}