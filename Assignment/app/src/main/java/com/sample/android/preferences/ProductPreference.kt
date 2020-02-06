package com.sample.android.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.sample.android.constants.Constants
import com.sample.android.model.ProductListModel

class ProductPreference {

    companion object {
        private val TAG = ProductPreference::class.java.simpleName
        private var mPreference: SharedPreferences? = null
        private var mInstance: ProductPreference? = null

        fun getInstance(context: Context): ProductPreference {
            if (mInstance == null) {
                mInstance = ProductPreference()
            }
            if (mPreference == null) {
                mPreference =
                    context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
            }
            return mInstance as ProductPreference
        }
    }

    fun addObjectToString(productListModel: ProductListModel) {
        try {
            val modelString = Gson().toJson(productListModel, ProductListModel::class.java)
            val editor = mPreference?.edit()
            editor?.putString(Constants.PREF_PRODUCT_LIST, modelString)
            editor?.apply()
        } catch (e: Exception) {
            Log.e(TAG, "addObjectToString: " + e.message)
        }
    }

    fun getObjectFromString(): ProductListModel {
        val stringModel = mPreference?.getString(Constants.PREF_PRODUCT_LIST, "")
        return Gson().fromJson(stringModel, ProductListModel::class.java)
    }
}