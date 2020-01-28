package com.android.dynamic.delivery.impl.helpers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.activities.CoreActivity

class Feature2Helper {

    companion object {
        private const val TAG: String = "Feature2Helper"
        private const val PACKAGE_NAME = "com.android.dynamic.feature2"
        const val MODULE_NAME = "DynamicFeature2"
        const val ACTIVITY_LAUNCH = "$PACKAGE_NAME.Feature2Activity"
        const val ACTIVITY_OTHER = "$PACKAGE_NAME.FeatureOtherActivity"

        fun launchFeature2(activity: CoreActivity, activityName: String, bundle: Bundle?) {
            try {
                val intent = Intent(activity, Class.forName(activityName))
                bundle?.let {
                    intent.putExtras(bundle)
                }
                activity.startActivity(intent)
                Log.d(TAG, "launchFeature2()")
            }catch (e: Exception){
                Log.e(TAG,"launchFeature2() ${e.message}")
            }
        }
    }
}