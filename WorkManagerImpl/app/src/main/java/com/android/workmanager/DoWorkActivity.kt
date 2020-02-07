package com.android.workmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.workmanager.utils.Constants
import com.android.workmanager.viewmodels.ApplyWorkViewModel
import kotlinx.android.synthetic.main.activity_do_work.*

class DoWorkActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSIONS = 101
    private lateinit var applyWorkViewModel: ApplyWorkViewModel
    private val sPermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_work)

        initWork(R.drawable.test)

        goBtn.setOnClickListener {
            applyWorkViewModel.applyFilterOnBitmap()
        }

        outputBtn.setOnClickListener {
            applyWorkViewModel.outImageUri?.let {

                /*val bitmap = BitmapFactory.decodeFile(it.toString())
                imageView.setImageBitmap(bitmap)*/

                val actionView = Intent(Intent.ACTION_VIEW, it)
                if (actionView.resolveActivity(packageManager) != null) {
                    startActivity(actionView)
                }
            }
        }

        requestPermissionsIfNecessary()
    }

    private fun initWork(@DrawableRes drawableResId: Int) {
        imageView.setImageResource(drawableResId)

        applyWorkViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(ApplyWorkViewModel::class.java)
        applyWorkViewModel.inputImgResId = drawableResId

        applyWorkViewModel.savedWorkInfo.observe(this, Observer { it ->
            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.
            if (!it.isNullOrEmpty()) {

                // We only care about the one output status.
                // Every continuation has only one worker tagged TAG_OUTPUT
                val workInfo = it[0]
                val isFinished = workInfo.state.isFinished
                if (isFinished) {
                    showWorkFinished()
                    val outputUri = workInfo.outputData.getString(Constants.KEY_OUTPUT_IMAGE_URI)
                    outputUri?.let {
                        applyWorkViewModel.outImageUri = Uri.parse(it)
                        outputBtn.visibility = View.VISIBLE
                    }
                } else {
                    showWorkInProgress()
                }
            }
        })
    }

    private fun showWorkInProgress() {
        progressBar.visibility = View.VISIBLE
        goBtn.visibility = View.GONE
        outputBtn.visibility = View.GONE
    }

    private fun showWorkFinished() {
        progressBar.visibility = View.GONE
        goBtn.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        applyWorkViewModel.cancelWork()
        super.onDestroy()
    }

    /**
     * Request permissions twice - if the user denies twice then show a toast about how to update
     * the permission for storage. Also disable the button if we don't have access to pictures on
     * the device.
     */
    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                sPermissions.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in sPermissions) {
            hasPermissions = hasPermissions and (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED)
        }
        return hasPermissions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }
}
