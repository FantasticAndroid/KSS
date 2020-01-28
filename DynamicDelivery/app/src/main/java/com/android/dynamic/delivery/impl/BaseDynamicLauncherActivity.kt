package com.android.dynamic.delivery.impl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.R
import com.android.dynamic.delivery.activities.CoreActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener

const val RC_FEATURE_REQUEST = 1024

abstract class BaseDynamicLauncherActivity : CoreActivity() {

    private lateinit var moduleTitleName: String
    protected lateinit var manager: SplitInstallManager
    protected var moduleName: String? = null
    protected var activityName: String? = null
    protected var bundle: Bundle? = null

    private var splitInstallStateUpdatedListener: SplitInstallStateUpdatedListener? = null
    protected abstract fun initSplitInstallListener(): SplitInstallStateUpdatedListener
    protected abstract fun installDynamicModule()
    protected abstract fun getModuleTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_launcher)

        bundle = intent.extras
        moduleName = bundle?.getString(DeliveryConst.KEY_MODULE_TITLE)
        activityName = bundle?.getString(DeliveryConst.KEY_ACTIVITY_TITLE)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = moduleTitleName

        moduleTitleName = getModuleTitle()
        manager = SplitInstallManagerFactory.create(this)
        splitInstallStateUpdatedListener = initSplitInstallListener()
        manager.registerListener(splitInstallStateUpdatedListener)
        installDynamicModule()
    }

    protected fun initiateSplitInstall() {
        val request = SplitInstallRequest.newBuilder()
                .addModule(moduleName)
                .build()
        // Load and install the requested feature module.
        manager.startInstall(request)
        manager.registerListener(splitInstallStateUpdatedListener)
   }


    override fun onDestroy() {
        super.onDestroy()
        splitInstallStateUpdatedListener?.let {
            manager.unregisterListener(splitInstallStateUpdatedListener)
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallStateUpdatedListener?.let {
            manager.registerListener(splitInstallStateUpdatedListener)
        }
    }

    /** This is needed to handle the result of the manager.startConfirmationDialogForResult
    request that can be made from SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION
    in the listener above. */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_FEATURE_REQUEST) {
            Log.d(TAG, "onActivityResult() resultCode$resultCode")
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }
    companion object {
        private val TAG: String = BaseDynamicLauncherActivity::class.java.simpleName
    }
}