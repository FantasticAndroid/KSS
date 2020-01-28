package com.android.dynamic.feature2

import android.os.Bundle
import com.android.dynamic.delivery.activities.CoreActivity

class FeatureOtherActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_other)
    }
}
