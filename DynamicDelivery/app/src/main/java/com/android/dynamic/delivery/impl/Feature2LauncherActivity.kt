package com.android.dynamic.delivery.impl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.R
import com.android.dynamic.delivery.activities.CoreActivity
import com.android.dynamic.delivery.impl.helpers.Feature2Helper
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_dynamic_launcher.*

class Feature2LauncherActivity : BaseDynamicLauncherActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getModuleTitle(): String {
        return dynamicApp.getString(R.string.title_dynamicfeature2)
    }

    override fun installDynamicModule() {
        if (manager.installedModules.contains(moduleName)) {
            Feature2Helper.launchFeature2(this, activityName!!, bundle)
            finish()
        } else {
            // Create request to install a feature module by name.
            messageTv.text = "Just a moment, a wonderful ${getModuleTitle()} experience is on its way!"
            initiateSplitInstall()
        }
    }

    override fun initSplitInstallListener(): SplitInstallStateUpdatedListener {
        return SplitInstallStateUpdatedListener { state ->

            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    stateTv.text = "Almost there..."
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    manager.startConfirmationDialogForResult(state, this@Feature2LauncherActivity, RC_FEATURE_REQUEST)
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    stateTv.text = "Here you go..."
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    launchFeature2()
                }
                SplitInstallSessionStatus.FAILED -> {
                    stateTv.text = "Installation Failed, Please try again!"
                }
            }
        }
    }

    private fun launchFeature2() {
       Feature2Helper.launchFeature2(this@Feature2LauncherActivity,
            activityName!!, bundle!!)
        finish()
        Log.d(TAG, "launchFeature2()")
    }

    companion object {
        private val TAG: String = Feature2LauncherActivity::class.java.simpleName
        fun startActivityWithResult(coreActivity: CoreActivity, requestCode: Int, bundle: Bundle) {
            val intent = Intent(coreActivity, Feature2LauncherActivity::class.java)
            intent.putExtras(bundle)
            coreActivity.startActivityForResult(intent, requestCode)
        }
    }
}