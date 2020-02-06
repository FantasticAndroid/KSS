package com.sample.android.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductModel {

    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("date_added")
    @Expose
    val dateAdded: String? = null
    @SerializedName("variants")
    @Expose
    val variantList: List<VariantModel>? = null
    @SerializedName("tax")
    @Expose
    val taxModel: TaxModel? = null

}