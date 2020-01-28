package com.android.dynamic.delivery.impl.helpers

import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.activities.CoreActivity
import com.android.dynamic.delivery.impl.DeliveryConst
import com.android.dynamic.delivery.impl.Feature1LauncherActivity
import com.android.dynamic.delivery.impl.Feature2LauncherActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory


class DynamicFeatureHelper {

    companion object {
        private const val TAG: String = "DynamicFeatureLauncher"

        fun launchFeature1(coreActivity: CoreActivity, bundle: Bundle) {
            bundle.putString(DeliveryConst.KEY_MODULE_TITLE, Feature1Helper.MODULE_NAME)

            val manager = SplitInstallManagerFactory.create(coreActivity)
            if (manager!!.installedModules.contains(Feature1Helper.MODULE_NAME)) {
                Feature1Helper.launchFeature1(coreActivity, bundle)
            } else {
                Feature1LauncherActivity.startActivityWithResult(
                    coreActivity,
                    DeliveryConst.KEY_RC_TO_MAIN, bundle
                )
            }
            Log.d(TAG, "launchFeature1()")
        }

        fun launchFeature2(coreActivity: CoreActivity, activityName: String?, bundle: Bundle) {
            bundle.putString(DeliveryConst.KEY_MODULE_TITLE, Feature2Helper.MODULE_NAME)
            bundle.putString(DeliveryConst.KEY_ACTIVITY_TITLE, activityName)
            if (!activityName.isNullOrBlank()) {
                val manager = SplitInstallManagerFactory.create(coreActivity)
                if (manager!!.installedModules.contains(Feature2Helper.MODULE_NAME)) {
                    Feature2Helper.launchFeature2(coreActivity, activityName, bundle)
                } else {
                    Feature2LauncherActivity.startActivityWithResult(
                        coreActivity,
                        DeliveryConst.KEY_RC_TO_MAIN, bundle
                    )
                }
            }
            Log.d(TAG, "launchFeature2()")
        }
    }
}