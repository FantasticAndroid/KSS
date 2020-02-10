package com.android.sample

import android.app.Application

class MainApp:Application() {
    override fun onCreate() {
        super.onCreate()
        initNativeLib()
    }

    private fun initNativeLib(){
       // System.loadLibrary("native-lib.cpp")
    }
}