package com.android.stethorelease

import android.content.Context
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.HurlStack

class VolleyUtil {

    companion object {

        fun getHttpTask(): BaseHttpStack {
            return HurlStack()
        }

        fun initStethoAccordingly(context: Context) {
            // Do not init Stetho in Release Build
            /*Stetho.initializeWithDefaults(context)*/
        }

        /*fun getVolley(context: Context):RequestQueue {
            return Volley.newRequestQueue(context)
        }

        fun getVolley(cacheDir:File, threadPoolSize:Int):RequestQueue {
            val network = BasicNetwork(HurlStack())
            return RequestQueue(DiskBasedCache(cacheDir), network, threadPoolSize)
        }*/
    }
}