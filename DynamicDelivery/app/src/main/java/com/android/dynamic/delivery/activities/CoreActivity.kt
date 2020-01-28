package com.android.dynamic.delivery.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.dynamic.delivery.DynamicApp

abstract class CoreActivity:AppCompatActivity() {

    protected lateinit var dynamicApp:DynamicApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dynamicApp = application as DynamicApp
    }
}