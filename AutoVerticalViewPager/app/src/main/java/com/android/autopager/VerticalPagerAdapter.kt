package com.android.autopager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VerticalPagerAdapter(activity: MainActivity, private val pagerList: Array<String>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return pagerList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PagerFragment.newInstance(pagerList[position])
    }
}