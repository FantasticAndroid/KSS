package com.android.stethorelease

import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.HurlStack

class VolleyUtil {

    companion object {

        fun getHttpTask(): BaseHttpStack {
            return HurlStack()
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