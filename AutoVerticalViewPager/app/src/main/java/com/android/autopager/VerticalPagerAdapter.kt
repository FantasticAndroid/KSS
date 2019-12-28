package com.android.autopager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.model.PagerModel
import com.android.sample.MainActivity
import com.android.sample.PagerFragment

class VerticalPagerAdapter(
    activity: MainActivity, private val autoVerticalListener: AutoVerticalListener,
    private val pagerList: List<PagerModel>
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return pagerList.size
    }

    override fun createFragment(position: Int): Fragment {
        val pagerModel = pagerList[position]
        return PagerFragment.newInstance(pagerModel, position, autoVerticalListener)
    }
}