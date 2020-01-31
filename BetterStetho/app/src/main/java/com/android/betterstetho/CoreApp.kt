package com.android.betterstetho

import android.app.Application
import com.android.stethodebug.RetroUtil

class CoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetroUtil.initStethoAccordingly(this)   // This will enable Stetho for Debug Build for All Retro/Volley and Other Inspection
        //or
        //VolleyUtil.initStethoAccordingly(this)
    }
}