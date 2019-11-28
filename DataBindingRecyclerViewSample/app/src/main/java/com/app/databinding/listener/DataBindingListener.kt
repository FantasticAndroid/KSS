package com.app.databinding.listener

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.databinding.R
import kotlinx.android.synthetic.main.activity_main.view.*

class DataBindingListener(private val mContext: Context) {

    fun onButtonClick() {
        Toast.makeText(mContext, "Button Clicked!", Toast.LENGTH_LONG).show()
    }

//    fun onMargin(): Float {
//        return mContext.resources.getDimension(R.dimen._36sdp)
//    }

    fun onSize(): Int {
        return mContext.resources.getDimensionPixelSize(R.dimen._14ssp)
    }
}