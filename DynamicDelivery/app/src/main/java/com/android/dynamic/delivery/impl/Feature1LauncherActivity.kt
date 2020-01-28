package com.android.dynamic.delivery.impl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.R
import com.android.dynamic.delivery.activities.CoreActivity
import com.android.dynamic.delivery.impl.helpers.Feature1Helper
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_dynamic_launcher.*

class Feature1LauncherActivity : BaseDynamicLauncherActivity() {

    override fun getModuleTitle(): String {
        return dynamicApp.getString(R.string.title_dynamicfeature1)
    }

    override fun installDynamicModule() {
        if (manager.installedModules.contains(moduleName)) {
            Feature1Helper.launchFeature1(this, bundle)
            finish()
        } else {
            // Create request to install a feature module by name.
            messageTv.text = getString(R.string.alert_feature1)
            initiateSplitInstall()
        }
    }

    override fun initSplitInstallListener(): SplitInstallStateUpdatedListener {
        return SplitInstallStateUpdatedListener { state ->

            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    stateTv.text = "Downloading Feature 1..."
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    manager.startConfirmationDialogForResult(state, this@Feature1LauncherActivity, RC_FEATURE_REQUEST)
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    stateTv.text = "installing Feature 1..."
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    launchFeature1()
                }
                SplitInstallSessionStatus.FAILED -> {
                    stateTv.text = "Feature 1 installation Failed, Please try again!"
                }
            }
        }
    }

    private fun launchFeature1() {
        Feature1Helper.launchFeature1(this, bundle)
        finish()
        Log.d(TAG, "launchFeature1()")
    }

    companion object {
        private val TAG: String = Feature1LauncherActivity::class.java.simpleName
        fun startActivityWithResult(coreActivity: CoreActivity, requestCode: Int, bundle: Bundle) {
            val intent = Intent(coreActivity, Feature1LauncherActivity::class.java)
            intent.putExtras(bundle)
            coreActivity.startActivityForResult(intent, requestCode)
        }
    }
}