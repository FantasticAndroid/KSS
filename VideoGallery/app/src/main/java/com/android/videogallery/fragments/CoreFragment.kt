package com.android.videogallery.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.android.VideoApp

abstract class CoreFragment : Fragment() {

    protected lateinit var videoApp:VideoApp

    override fun onAttach(context: Context) {
        super.onAttach(context)
        videoApp = context.applicationContext as VideoApp
    }
}