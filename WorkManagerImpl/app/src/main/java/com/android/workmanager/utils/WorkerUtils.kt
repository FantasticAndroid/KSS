package com.android.workmanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class WorkerUtils {

    companion object {
        private val TAG = WorkerUtils::class.java.simpleName

        @WorkerThread
        fun applyBlackFilter(bitmap: Bitmap): Bitmap {
            return ImageFilters().applyBlackFilter(bitmap)
        }

        @WorkerThread
        fun applyReflection(bitmap: Bitmap): Bitmap {
            return ImageFilters().applyReflection(bitmap)
        }

        fun writeBitmapToGallery(context: Context, bitmap: Bitmap): Uri {
            val savedImageURL = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "work-output",
                "Apply Worker"
            )
            return Uri.parse(savedImageURL)
        }

        /**
         * Writes bitmap to a temporary file and returns the Uri for the file
         * @param applicationContext Application context
         * @param bitmap Bitmap to write to temp file
         * @return Uri for temp file with bitmap
         * @throws FileNotFoundException Throws if bitmap file cannot be found
         */
        @Throws(FileNotFoundException::class)
        fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {

            val name = String.format("work-output-%s.png", UUID.randomUUID().toString())

            ////val root = applicationContext.getExternalFilesDir()

            val outputDir = File(
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Constants.OUTPUT_PATH
            )

            if (!outputDir.exists()) {
                outputDir.mkdirs() // should succeed
            }
            val outputFile = File(outputDir, name)
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(outputFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
            } finally {
                try {
                    out?.close()
                } catch (ignore: IOException) {
                }
            }
            return Uri.fromFile(outputFile)
        }

        fun sleep() {
            try {
                Thread.sleep(Constants.DELAY_TIME_MILLIS, 0)
            } catch (e: InterruptedException) {
                Log.d(TAG, Objects.requireNonNull<String>(e.message))
            }
        }
    }
}
