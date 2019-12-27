package com.android.autopagerlib

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class AutoVerticalPagerAdapter : FragmentStateAdapter {

    constructor (fragment: Fragment) : super(fragment)
    constructor (fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor (fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager, lifecycle
    )


}
