package com.app.databinding.model

import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName

class StateKt {
    @Bindable
    @SerializedName("id")
    var id: String? = null
    @Bindable
    @SerializedName("name")
    var name: String? = null
    @Bindable
    @SerializedName("nameInEnglish")
    var nameInEnglish: String? = null
}