package com.android.autopager.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PagerModel(
    @SerializedName("id") val pageId: String,
    @SerializedName("page_name") val pageName: String,
    @SerializedName("next_page_name")
    val nextPageName: String
) : Serializable