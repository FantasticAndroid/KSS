package com.app.databinding.model

import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StateListKtResponse {
    @SerializedName("code")
    var code: Int = 0
    @SerializedName("message")
    var message: String? = null
    @SerializedName("time")
    @Expose
    var time: Int = 0
    @Bindable
    @SerializedName("data")
    @Expose
    var stateListData: StateListData? = null

    class StateListData {
        @Bindable
        @SerializedName("states")
        @Expose
        var stateList: List<StateKt>? = null
    }
}