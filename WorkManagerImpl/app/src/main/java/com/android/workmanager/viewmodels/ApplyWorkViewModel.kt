package com.android.workmanager.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.workmanager.utils.Constants
import com.android.workmanager.workers.ApplyWorker

class ApplyWorkViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)
    var inputImgResId: Int? = null
    var outImageUri: Uri? = null

    val savedWorkInfo = workManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT)

    fun applyFilterOnBitmap() {
        val builder = Data.Builder()
        inputImgResId?.let {
            builder.putInt(Constants.KEY_INPUT_IMAGE_RES, it)
        }
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(ApplyWorker::class.java)
            .addTag(Constants.TAG_OUTPUT)
            .setInputData(builder.build())
            .build()
        workManager.enqueue(oneTimeWorkRequest)
    }

    fun cancelWork() {
        workManager.cancelAllWorkByTag(Constants.TAG_OUTPUT)
    }
}