package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class TaxModel {
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("value")
    @Expose
    val value: Double? = 0.0

}