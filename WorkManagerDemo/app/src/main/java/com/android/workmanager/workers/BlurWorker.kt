package com.android.workmanager.workers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.workmanager.R
import com.android.workmanager.utils.Constants

/**
 * Creates an instance of the {@link Worker}.
 *
 * @param appContext   the application {@link Context}
 * @param workerParams the set of {@link WorkerParameters}
 */
class BlurWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {

        try {
            // Makes a notification when the work starts and slows down the work so that it's easier to
            // see each WorkRequest start, even on emulated devices
            /*WorkerUtils.makeStatusNotification("Blurring image", applicationContext)
                WorkerUtils.sleep()*/

            val imageUri = inputData.getString(Constants.KEY_IMAGE_URI)
            lateinit var bitmap: Bitmap
            imageUri?.let {
                bitmap = BitmapFactory.decodeStream(
                    appContext.contentResolver.openInputStream(
                        Uri.parse(imageUri)
                    )
                )
            } ?: run {
                val imageResId = inputData.getInt(Constants.KEY_IMAGE_RES,R.drawable.test)
                bitmap = BitmapFactory.decodeResource(appContext.resources, imageResId)
            }

            /*val output = WorkerUtils.blurBitmap(bitmap, appContext)
            // Write bitmap to a temp file
            val outputUri = WorkerUtils.writeBitmapToFile(appContext, output)
            // Return the output for the temp file
            val outputData = Data.Builder().putString(
                Constants.KEY_IMAGE_URI, outputUri.toString()
            ).build()

            return Result.success(outputData)*/

            return Result.failure()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}