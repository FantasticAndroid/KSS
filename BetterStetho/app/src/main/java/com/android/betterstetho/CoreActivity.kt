package com.android.betterstetho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class CoreActivity : AppCompatActivity() {

    protected lateinit var coreApp: CoreApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coreApp = application as CoreApp
    }
}