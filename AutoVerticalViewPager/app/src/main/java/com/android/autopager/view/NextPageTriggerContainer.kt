package com.android.autopager.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.android.autopager.callback.OnScrollRefreshListener

class NextPageTriggerContainer : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var y1 = 0f
    private val compareDelta = -200f
    private var onScrollRefreshListener: OnScrollRefreshListener? = null
    /***
     *
     * @param onScrollRefreshListener
     */
    fun detectBottomToTopSwipe(onScrollRefreshListener: OnScrollRefreshListener) {
        this.onScrollRefreshListener = onScrollRefreshListener
        /*setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onScrollRefreshListener.onBottomToTopSwiped();
            }
        });*/
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return try {
            parent.requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    y1 = event.y
                    return true
                }
                MotionEvent.ACTION_MOVE -> return true // super.onTouchEvent(event);
                MotionEvent.ACTION_CANCEL -> return true
                MotionEvent.ACTION_UP -> {
                    val y2 = event.y
                    val deltaY = y2 - y1
                    ////Log.d("StoryCardView", "onTouchEvent: deltaY:compareDelta " + deltaY + " : " + compareDelta);
                    if (deltaY < compareDelta) {
                        if (onScrollRefreshListener != null) {
                            onScrollRefreshListener!!.onBottomToTopSwiped()
                            parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    return true
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.onTouchEvent(event)
        }
        ////return super.onTouchEvent(event);
    }
}