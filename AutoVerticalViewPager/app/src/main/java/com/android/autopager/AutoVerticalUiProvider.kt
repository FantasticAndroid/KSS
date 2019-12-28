package com.android.autopager

import android.content.Context
import android.graphics.Rect
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.android.MainApp
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.callback.OnScrollRefreshListener
import com.android.autopager.model.PagerModel
import com.android.autopager.view.NestedRefreshView
import com.android.autopager.view.NextPageTriggerContainer

class AutoVerticalUiProvider(
    private val mainApp: Context,
    private val autoVerticalListener: AutoVerticalListener?,
    view: View
) :
    View.OnClickListener {

    private val nestedRefreshView: NestedRefreshView = view.findViewById(R.id.nestedRefreshView)
    private val nextPageHeadingTv: TextView = view.findViewById(R.id.tv_nxt_page_header)
    private val nextPageTriggerContainer: NextPageTriggerContainer =
        view.findViewById(R.id.nextPageTriggerContainer)
    private val counterProgressBar: ProgressBar = view.findViewById(R.id.counterProgressBar)
    private val counterProgressTv: TextView = view.findViewById(R.id.tv_progress)
    private val cancelCounterTv: TextView = view.findViewById(R.id.tv_cancel_counter)
    private val nextPageNameTv: TextView = view.findViewById(R.id.nxtPageNameTv)

    private val bottomMargin = 0
    private var nextPageCountDownTimer: NextPageCountDownTimer? = null
    private var startProgress = 0
    private val TICK_COUNTS = 5
    private val TICK_INTERVAL_DURATION = 1000
    private var scrollHandler: Handler? = null
    /*private var nextStoryLoadingTitle: String? = null
    private var swipeToNextStoryTitle: String? = null*/
    private var isArticleEndTrack = false

    fun initProvider() {
        operateNestedRefreshView()
        scrollHandler = Handler(mainApp.mainLooper)
        cancelCounterTv.setOnClickListener(this)
        handlerNestedRefreshViewScroll()
    }

    private fun operateNestedRefreshView() {
        nextPageTriggerContainer.detectBottomToTopSwipe(onScrollRefreshListener)
        nestedRefreshView.setScrollDetectionParams(object :
            NestedRefreshView.OnScrollDetectionListener {
            override fun onScrolledToTop() {
                Log.d(
                    TAG, "onScrollChange: NestedScrollView reached to TOP"
                )
                //Toast.makeText(context, "NestedScrollView reached to TOP", Toast.LENGTH_SHORT).show();
            }

            override fun onScrolledToBottom() {
                Log.d(
                    TAG, "onScrollChange: NestedScrollView reached to Bottom"
                )
                //Toast.makeText(getContext(), "NestedScrollView reached to Bottom", Toast.LENGTH_SHORT).show();
                startNextPageCounter()
            }
        }, NestedRefreshView.ScrollDetectDirection.BOTH)
        nestedRefreshView.setOnScrollEndListener {
            Log.d(TAG, "setOnScrollEndListener: onScrollEnded")
            // Toast.makeText(getContext(), "setOnScrollEndListener onScrollEnded ENDED", Toast.LENGTH_SHORT).show();
            handleNextPageCounterUi()
        }
        nestedRefreshView.setOnScrollRefreshListener(onScrollRefreshListener)
    }

    fun hideNextPageCardView() {
        nextPageHeadingTv.visibility = View.GONE
        nextPageTriggerContainer.visibility = View.GONE
        nextPageTriggerContainer.tag = null
    }

    /***
     *
     * @param pagerModel
     */
    fun setNextPageContainer(pagerModel: PagerModel?) {
        nextPageHeadingTv.visibility = View.VISIBLE
        nextPageTriggerContainer.visibility = View.VISIBLE
        nextPageTriggerContainer.tag = pagerModel
        nextPageNameTv.text = pagerModel?.nextPageName
    }

    private fun handleNextPageCounterUi() {
        try {
            val isCardVisible: Boolean =
                isNextPageCounterUiCompletelyVisible(nextPageTriggerContainer)
            Log.d(
                TAG,
                "handleNextPageCounterUi() isCardVisible: $isCardVisible"
            )
            if (!isCardVisible) { //Toast.makeText(dbApplication, "LOADER_STOPPED", Toast.LENGTH_SHORT).show();
                cancelNextPageCounter()
            }
            if (isCardVisible && !isArticleEndTrack) {
                isArticleEndTrack = true
                Log.d(TAG, "Event.ARTICLE_END")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun isNextPageCounterUiCompletelyVisible(nextStoryCv: NextPageTriggerContainer): Boolean {
        val isNextPageCounterHiddenCompletely: Boolean =
            isNextPageCounterUiCompletelyHidden(nextStoryCv)
        Log.d(TAG, "isNextPageCounterHiddenCompletely: $isNextPageCounterHiddenCompletely")
        return if (!isNextPageCounterHiddenCompletely) {
            val scrollBounds = Rect()
            nestedRefreshView.getDrawingRect(scrollBounds)
            val top: Float = nextStoryCv.y
            val bottom: Float = top + nextStoryCv.height + bottomMargin
            Log.d(
                TAG,
                "CardVisibility " + "scrollBounds.top : top " + scrollBounds.top + " : " + top
            )
            Log.d(
                TAG,
                "CardVisibility " + "scrollBounds.bottom : bottom " + scrollBounds.bottom + " : " + bottom
            )
            if (scrollBounds.top <= top && scrollBounds.bottom >= bottom) {
                Log.d(TAG, "CardCompletelyVisible TRUE")
                true
            } else {
                Log.d(TAG, "CardCompletelyVisible FALSE")
                false
            }
        } else {
            false
        }
    }

    private fun isNextPageCounterUiCompletelyHidden(nextStoryCv: NextPageTriggerContainer): Boolean {
        val scrollBounds = Rect()
        nestedRefreshView.getHitRect(scrollBounds)
        return !nextStoryCv.getLocalVisibleRect(scrollBounds)
    }

    fun cancelNextPageCounter() {
        Log.d(TAG, "cancelNextStoryCounter()")
        startProgress = 0
        nextPageCountDownTimer?.cancel()
        nextPageCountDownTimer = null
        counterProgressBar.visibility = View.GONE
        counterProgressTv.visibility = View.GONE
        cancelCounterTv.visibility = View.GONE
        counterProgressTv.text = ""
        counterProgressBar.progress = 0
        nextPageHeadingTv.text = mainApp.getString(R.string.label_swipe_next_page)
    }

    private fun startNextPageCounter() {
        if (nextPageCountDownTimer == null) {
            startProgress = 0
            counterProgressBar.visibility = View.VISIBLE
            counterProgressTv.visibility = View.VISIBLE
            cancelCounterTv.visibility = View.VISIBLE
            counterProgressBar.max = TICK_COUNTS
            nextPageCountDownTimer = NextPageCountDownTimer(
                (TICK_INTERVAL_DURATION * (TICK_COUNTS + 1)).toLong(),
                TICK_INTERVAL_DURATION.toLong()
            )
            nextPageCountDownTimer!!.start()
            nextPageHeadingTv.text = mainApp.getString(R.string.label_load_next_page)
        }
    }

    /**
     * @param isAutoSwiped
     */
    private fun triggerEventToShowNextPage(isAutoSwiped: Boolean) {
        if (!isPageScrollingLive()) {
            cancelNextPageCounter()
            autoVerticalListener?.onShowingNextPage(isAutoSwiped)
            handlerNestedRefreshViewScroll()
            applyScrollingToTop()
            /*checkWhetherTutorial_2_Shown()*/
        }
    }

    private fun applyScrollingToTop() {
        scrollHandler?.postDelayed(
            nestedScrollingToTopCallback,
            Util.ANIMATE_SCROLL_DURATION * 2 + 50
        )
    }

    private fun handlerNestedRefreshViewScroll() {
        nestedRefreshView.setScrollingEnabled(false)
        scrollHandler?.postDelayed(
            nestedScrollEnableCallback,
            Util.ANIMATE_SCROLL_DURATION * 2
        )
    }

    private fun triggerEventToShowPreviousPage() {
        cancelNextPageCounter()
        autoVerticalListener?.onShowingPreviousPage()
        handlerNestedRefreshViewScroll()
    }

    private fun isPageScrollingLive(): Boolean {
        return nestedRefreshView.isScrollingLive
    }

    private val nestedScrollingToTopCallback = Runnable {
        nestedRefreshView.fling(0)
        nestedRefreshView.smoothScrollTo(0, 5)
    }

    private val nestedScrollEnableCallback =
        Runnable { nestedRefreshView.setScrollingEnabled(true) }

    private val onScrollRefreshListener: OnScrollRefreshListener =
        object : OnScrollRefreshListener {
            override fun onBottomToTopSwiped() {
                triggerEventToShowNextPage(false)
            }

            override fun onTopToBottomSwiped() {
                triggerEventToShowPreviousPage()
            }
        }

    private inner class NextPageCountDownTimer
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
    constructor(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val count: Int = TICK_COUNTS - startProgress
            if (count >= 0) {
                counterProgressTv.text = count.toString()
                counterProgressBar.progress = startProgress
                startProgress = ++startProgress
            }
        }

        override fun onFinish() {
            try {
                triggerEventToShowNextPage(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "NextStoryCountDownTimer(onFinish)".plus(e.message))
            }
        }
    }

    override fun onClick(view: View?) {
        cancelNextPageCounter()
    }

    fun onDestroyUiView() {
        try {
            if (nextPageCountDownTimer != null) {
                nextPageCountDownTimer!!.cancel()
            }
            if (scrollHandler != null) {
                scrollHandler!!.removeCallbacks(nestedScrollingToTopCallback)
                scrollHandler!!.removeCallbacks(nestedScrollEnableCallback)
                scrollHandler = null
            }
        } catch (e: Exception) {
            Log.e(TAG, "onDestroyUiView()".plus(e.message))
        }
    }

    companion object {
        private val TAG: String =
            AutoVerticalUiProvider::class.java.simpleName
    }
}