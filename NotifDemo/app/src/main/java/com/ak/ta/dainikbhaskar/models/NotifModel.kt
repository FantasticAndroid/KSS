package com.ak.ta.dainikbhaskar.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class NotifModel() : Parcelable {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("catId")
    var catId: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    @SerializedName("pcat_id")
    var pCatId: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    @SerializedName("storyId")
    var storyId: String? = null

    @SerializedName("deeplink_url")
    var deeplink: String? = null

    @SerializedName("pushtype")
    var pushType: String? = null
        get() {
            return if (field.isNullOrEmpty()) "0" else field
        }

    @SerializedName("title")
    var title: String? = null

    @SerializedName("color")
    var color: String? = null

    @SerializedName("img_url")
    var imageURL: String? = null

    @SerializedName("action_txt")
    var actionText: String? = null

    //for catId 6
    @SerializedName("brandId")
    var brandId: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    @SerializedName("menuId")
    var menuId: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    //for catId 7
    @SerializedName("webUrl")
    var mWeburl: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    @SerializedName("webTitle")
    var mWebTitle: String? = null
        get() {
            return if (field.isNullOrEmpty()) "" else field
        }

    @SerializedName("webExt")
    var wapExt: String? = null
        get() {
            return if (field.isNullOrEmpty()) "0" else field
        }

    @SerializedName("sl_id")
    var slId: String? = null

    @SerializedName("sign")
    var sign: String? = null

    @SerializedName("score_update")
    private var scoreUpdateInfo: String? = null

    @SerializedName("personalize")
    private var personalizeInfo: String? = null

    @SerializedName("priority")
    var priority: String? = null
        get() {
            return if (field.isNullOrEmpty()) "0" else field
        }

    constructor(parcel: Parcel) : this() {
        message = parcel.readString()
        catId = parcel.readString()
        pCatId = parcel.readString()
        storyId = parcel.readString()
        deeplink = parcel.readString()
        title = parcel.readString()
        color = parcel.readString()
        imageURL = parcel.readString()
        actionText = parcel.readString()
        brandId = parcel.readString()
        menuId = parcel.readString()
        mWeburl = parcel.readString()
        mWebTitle = parcel.readString()
        slId = parcel.readString()
        sign = parcel.readString()
        scoreUpdateInfo = parcel.readString()
        personalizeInfo = parcel.readString()
        priority = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(catId)
        parcel.writeString(pCatId)
        parcel.writeString(storyId)
        parcel.writeString(deeplink)
        parcel.writeString(title)
        parcel.writeString(color)
        parcel.writeString(imageURL)
        parcel.writeString(actionText)
        parcel.writeString(brandId)
        parcel.writeString(menuId)
        parcel.writeString(mWeburl)
        parcel.writeString(mWebTitle)
        parcel.writeString(slId)
        parcel.writeString(sign)
        parcel.writeString(scoreUpdateInfo)
        parcel.writeString(personalizeInfo)
        parcel.writeString(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotifModel> {
        override fun createFromParcel(parcel: Parcel): NotifModel {
            return NotifModel(parcel)
        }

        override fun newArray(size: Int): Array<NotifModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "NotifModel(message=$message, storyId=$storyId, deeplink=$deeplink, title=$title, color=$color, imageURL=$imageURL, actionText=$actionText, slId=$slId, sign=$sign, scoreUpdateInfo=$scoreUpdateInfo, personalizeInfo=$personalizeInfo)"
    }
}