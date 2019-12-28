package com.android.autopager

import android.content.Context
import android.util.Log
import java.nio.charset.Charset

class Util {

    companion object {
        val KEY_PAGE_NAME = "key_page_name"
        val KEY_POSITION = "key_position"
        const val ANIMATE_SCROLL_DURATION = 500L

        /**
         * This method will be used to read json from file
         *
         * @param context
         * @param fileName
         */
        fun readFileFromJson(
            context: Context, fileName: String
        ): String? {
            var json: String? = null
            try {
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charset.forName("UTF-8"))
                //Log.d("JSON", "Read JSON from file: $json")
            } catch (e: Exception) {
                Log.e("readJSON", "readJSON: " + e.message)
            }
            return json
        }
    }


}