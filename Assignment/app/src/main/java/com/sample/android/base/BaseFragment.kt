package com.sample.android.base

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    var mBaseActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mBaseActivity = context
        }
    }
}