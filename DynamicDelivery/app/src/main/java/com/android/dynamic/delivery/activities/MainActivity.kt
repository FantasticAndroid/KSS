package com.android.dynamic.delivery.activities

import android.os.Bundle
import android.view.View
import com.android.dynamic.delivery.R
import com.android.dynamic.delivery.impl.helpers.DynamicFeatureHelper
import com.android.dynamic.delivery.impl.helpers.Feature1Helper
import com.android.dynamic.delivery.impl.helpers.Feature2Helper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        feature1Btn.setOnClickListener(clickListener)
        feature2Btn.setOnClickListener(clickListener)
        featureOtherBtn.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener {

        when (it.id) {
            R.id.feature1Btn -> {
                val bundle = Bundle()
                DynamicFeatureHelper.launchFeature1(this@MainActivity,bundle)
            }
            R.id.feature2Btn -> {
                val bundle = Bundle()
                DynamicFeatureHelper.launchFeature2(this@MainActivity,
                    Feature2Helper.ACTIVITY_LAUNCH,bundle)
            }
            R.id.featureOtherBtn -> {
                val bundle = Bundle()
                DynamicFeatureHelper.launchFeature2(this@MainActivity,
                    Feature2Helper.ACTIVITY_OTHER,bundle)
            }
        }
    }
}
