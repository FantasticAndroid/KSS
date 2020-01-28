package com.android.dynamic.delivery.impl.helpers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.dynamic.delivery.activities.CoreActivity

class Feature1Helper {

    companion object {
        private const val TAG: String = "Feature1Helper"
        private const val PACKAGE_NAME = "com.android.dynamic.feature1"
        const val MODULE_NAME = "DynamicFeature1"
        private const val ACTIVITY_LAUNCH = "$PACKAGE_NAME.Feature1Activity"

        fun launchFeature1(activity: CoreActivity, bundle: Bundle?) {
            try {
                val intent = Intent(activity, Class.forName(ACTIVITY_LAUNCH))
                bundle?.let {
                    intent.putExtras(bundle)
                }
                activity.startActivity(intent)
                Log.d(TAG, "launchFeature1()")
            }catch (e: Exception){
                Log.e(TAG,"launchFeature1() ${e.message}")
            }
        }
    }
}