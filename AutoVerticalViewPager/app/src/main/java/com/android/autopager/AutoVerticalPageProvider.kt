package com.android.autopager

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.autopager.callback.AutoVerticalListener

class AutoVerticalPageProvider(private val autoPager: ViewPager2) : AutoVerticalListener {

    companion object {
        private val TAG = AutoVerticalPageProvider::class.java.simpleName
    }

    override fun getViewPagerAnimationRunning(): Boolean {
        return try {
            (autoPager.getChildAt(0) as RecyclerView).itemAnimator!!.isRunning
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "isViewPagerAnimationRunning() " + e.message
            )
            false
        }
    }

    override fun onShowingNextPage(
        isAutoSwiped: Boolean
    ) {
        try {
            if (!getViewPagerAnimationRunning()) {
                autoPager.setCurrentItem(
                    autoPager.currentItem + 1,
                    true
                )
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                e.message + ""
            )
        }
    }

    override fun onShowingPreviousPage() {
        try {
            if (autoPager.currentItem > 0) {
                /********** MANUAL PAGE UP/DOWN  */
                if (!getViewPagerAnimationRunning()) { /*animatePagerTransition(false);*/
                    autoPager.setCurrentItem(
                        autoPager.currentItem - 1,
                        true
                    )
                }
                /********** MANUAL PAGE UP/DOWN  */
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message + "")
        }
    }
}