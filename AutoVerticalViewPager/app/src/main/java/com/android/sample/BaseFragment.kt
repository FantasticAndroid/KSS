package com.android.sample

import android.content.Context
import androidx.fragment.app.Fragment
import com.android.MainApp

abstract class BaseFragment :Fragment() {

    protected var mainApp:MainApp?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainApp = context.applicationContext as MainApp
    }
}