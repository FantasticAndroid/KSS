package com.android.workmanager.workers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.workmanager.R
import com.android.workmanager.utils.Constants
import com.android.workmanager.utils.WorkerUtils

/**
 * Creates an instance of the {@link Worker}.
 *
 * @param appContext   the application {@link Context}
 * @param workerParams the set of {@link WorkerParameters}
 */
class ApplyWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        try {
            // Makes a notification when the work starts and slows down the work so that it's easier to
            // see each WorkRequest start, even on emulated devices
            /*WorkerUtils.makeStatusNotification("Blurring image", applicationContext)
                WorkerUtils.sleep()*/

            val imageResId = inputData.getInt(Constants.KEY_INPUT_IMAGE_RES, R.drawable.test)
            val bitmap = BitmapFactory.decodeResource(appContext.resources, imageResId)

            // Write bitmap to a temp file
            val output = WorkerUtils.applyBlackFilter(WorkerUtils.applyReflection(bitmap))
            /*val outputUri = WorkerUtils.writeBitmapToFile(appContext, output)*/
            val outputUri = WorkerUtils.writeBitmapToGallery(appContext, output)

            // Return the output for the temp file
            val outputData = Data.Builder().putString(
                Constants.KEY_OUTPUT_IMAGE_URI, outputUri.toString()
            ).build()

            return Result.success(outputData)

            ////return Result.failure()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}