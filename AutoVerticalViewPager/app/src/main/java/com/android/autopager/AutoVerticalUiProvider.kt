package com.android.autopager

import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.android.MainApp
import com.android.autopager.callback.OnScrollRefreshListener
import com.android.autopager.view.NestedRefreshView
import com.android.autopager.view.NextPageTriggerContainer

class AutoVerticalUiProvider(private val mainApp: MainApp, view: View) : BaseProvider(mainApp),View.OnClickListener {

    private val nestedRefreshView: NestedRefreshView = view.findViewById(R.id.nestedRefreshView)
    private val nextStoryHeadingTv: TextView = view.findViewById(R.id.tv_nxt_page_header)
    private val nextStoryContainer: NextPageTriggerContainer =
        view.findViewById(R.id.nextPageTriggerContainer)
    private val counterProgressBar: ProgressBar = view.findViewById(R.id.counterProgressBar)
    private val counterProgressTv: TextView = view.findViewById(R.id.tv_progress)
    private val cancelCounterTv: TextView = view.findViewById(R.id.tv_cancel_counter)

    private val bottomMargin = 0
    private var nextStoryCountDownTimer: NextStoryCountDownTimer? = null
    private var startProgress = 0
    private val TICK_COUNTS = 5
    private val TICK_INTERVAL_DURATION = 1000
    private var scrollHandler: Handler? = null
    /*private var nextStoryLoadingTitle: String? = null
    private var swipeToNextStoryTitle: String? = null*/
    private var isArticleEndTrack = false
    private val storyTitle = ""

    init {
        operateNestedRefreshView()
        scrollHandler = Handler(mainApp.mainLooper)
        cancelCounterTv.setOnClickListener(this)
    }

    private fun operateNestedRefreshView() {
        nextStoryContainer.detectBottomToTopSwipe(onScrollRefreshListener)
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
                startNextStoryCounter()
            }
        }, NestedRefreshView.ScrollDetectDirection.BOTH)
        nestedRefreshView.setOnScrollEndListener(object : NestedRefreshView.OnScrollEndListener {
            override fun onScrollEnded() {
                Log.d(
                    TAG, "setOnScrollEndListener: onScrollEnded"
                )
                // Toast.makeText(getContext(), "setOnScrollEndListener onScrollEnded ENDED", Toast.LENGTH_SHORT).show();
                handleNextStoryCounterUi()
            }
        })
        nestedRefreshView.setOnScrollRefreshListener(onScrollRefreshListener)
    }

    private fun handleNextStoryCounterUi() {
        try {
            val isCardVisible: Boolean =
                isNextStoryCounterUiCompletelyVisible(nextStoryContainer)
            Log.d(
                TAG,
                "handleNextStoryCounterUi() isCardVisible: $isCardVisible"
            )
            if (!isCardVisible) { //Toast.makeText(dbApplication, "LOADER_STOPPED", Toast.LENGTH_SHORT).show();
                cancelNextStoryCounter()
            }
            if (isCardVisible && !isArticleEndTrack) {
                isArticleEndTrack = true
                Log.d(TAG, "Event.ARTICLE_END")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun cancelNextStoryCounter() {
        Log.d(TAG, "cancelNextStoryCounter()")
        startProgress = 0
        nextStoryCountDownTimer?.cancel()
        nextStoryCountDownTimer = null
        counterProgressBar.visibility = View.GONE
        counterProgressTv.visibility = View.GONE
        cancelCounterTv.visibility = View.GONE
        counterProgressTv.text = ""
        counterProgressBar.progress = 0
        nextStoryHeadingTv.text = mainApp.getString(R.string.label_swipe_next_page)
    }

    private fun startNextStoryCounter() {
        if (nextStoryCountDownTimer == null) {
            startProgress = 0
            counterProgressBar.visibility = View.VISIBLE
            counterProgressTv.visibility = View.VISIBLE
            cancelCounterTv.visibility = View.VISIBLE
            counterProgressBar.max = TICK_COUNTS
            nextStoryCountDownTimer = NextStoryCountDownTimer(
                (TICK_INTERVAL_DURATION * (TICK_COUNTS + 1)).toLong(),
                TICK_INTERVAL_DURATION.toLong()
            )
            nextStoryCountDownTimer!!.start()
            nextStoryHeadingTv.text = mainApp.getString(R.string.label_load_next_page)
        }
    }


    /**
     * @param isAutoSwiped
     */
    private fun triggerEventToShowNextStory(isAutoSwiped: Boolean) {
        if (!isPageScrollingLive()) {
            cancelNextStoryCounter()
            newsDetailVerticalFragment.onNextStoryCardSwiped(isAutoSwiped)
            handlerNestedRefreshViewScroll()
            applyScrollingToTop()
            checkWhetherTutorial_2_Shown()
        }
    }


    private val onScrollRefreshListener: OnScrollRefreshListener =
        object : OnScrollRefreshListener {
            override fun onBottomToTopSwiped() {
                triggerEventToShowNextStory(false)
            }

            override fun onTopToBottomSwiped() {
                triggerEventToShowPreviousStory()
            }
        }

    private inner class NextStoryCountDownTimer
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
                triggerEventToShowNextStory(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "NextStoryCountDownTimer(onFinish)".plus(e.message))
            }
        }
    }


    override fun onClick(view: View?) {
        cancelNextStoryCounter()
    }

    companion object {
        private val TAG: String =
            AutoVerticalUiProvider::class.java.simpleName
    }
}