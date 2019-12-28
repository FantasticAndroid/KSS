package com.android.autopager.callback

interface AutoVerticalListener {
    fun onShowingNextPage(isAutoSwiped: Boolean)
    fun onShowingPreviousPage()
    fun getViewPagerAnimationRunning() : Boolean
}