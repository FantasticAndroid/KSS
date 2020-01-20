package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class VariantModel {
    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("color")
    @Expose
    val color: String? = null
    @SerializedName("size")
    @Expose
    val size: Any? = null
    @SerializedName("price")
    @Expose
    val price: Int? = null

}