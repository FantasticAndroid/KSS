package com.android.autopager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.android.autopager.callback.OnScrollRefreshListener;

public final class NestedRefreshView extends NestedScrollView {

    private ScrollDetectDirection scrollDetectDirection;
    private OnScrollDetectionListener onScrollDetectionListener;
    private static final String TAG = NestedRefreshView.class.getSimpleName();

    private OnScrollEndListener onScrollEndListener;
    private Runnable scrollerTask;
    private int initialPosition;

    private int newCheck = 100;

    private boolean isScrollingLive = false;
    private float compareDelta = 200f;

    private boolean isScrollerInTop = true;
    private OnScrollRefreshListener onScrollRefreshListener;

    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean isScrollable = true;

    public void setScrollingEnabled(boolean enabled) {
        isScrollable = enabled;
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    /***
     *
     * @param onScrollDetectionListener
     * @param scrollDetectDirection
     */
    public void setScrollDetectionParams(@NonNull OnScrollDetectionListener onScrollDetectionListener,
                                         @NonNull ScrollDetectDirection scrollDetectDirection) {
        this.scrollDetectDirection = scrollDetectDirection;
        this.onScrollDetectionListener = onScrollDetectionListener;
    }

    /***
     *
     * @param listener
     */
    public void setOnScrollEndListener(@NonNull OnScrollEndListener listener) {
        this.onScrollEndListener = listener;
        initScroller();
    }

    /***
     *
     * @param listener
     */
    public void setOnScrollRefreshListener(@NonNull OnScrollRefreshListener listener) {
        this.onScrollRefreshListener = listener;
    }

    public NestedRefreshView(@NonNull Context context) {
        super(context);
    }

    public NestedRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);

    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);

        if (!isScrollable) {
            return;
        }

        /*Log.d(TAG, "onScrollChanged oldScrollY : scrollY" + oldScrollY + " : " + scrollY);*/
        isScrollerInTop = false;
        try {
            if (scrollDetectDirection == ScrollDetectDirection.BOTTOM || scrollDetectDirection == ScrollDetectDirection.BOTH) {
                if ((getChildAt(getChildCount() - 1).getBottom() - (getHeight() + scrollY)) <= 0) {
                    onScrollDetectionListener.onScrolledToBottom();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
        if (scrollDetectDirection == ScrollDetectDirection.TOP || scrollDetectDirection == ScrollDetectDirection.BOTH) {
            if (scrollY == 0) {
                onScrollDetectionListener.onScrolledToTop();
                isScrollerInTop = true;
            }
        }
    }

    private void startScrollerTask() {
        initialPosition = getScrollY();
        NestedRefreshView.this.postDelayed(scrollerTask, newCheck);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);

    }

    private float y1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!isScrollable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // if we can scroll pass the event to the superclass
                    return isScrollable && super.onTouchEvent(ev);
                default:
                    return super.onTouchEvent(ev);
            }
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y1 = ev.getY();
                    isScrollingLive = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    isScrollingLive = true;
                    break;
                case MotionEvent.ACTION_UP:
                    float y2 = ev.getY();
                    float deltaY = y2 - y1;

                    ///Log.d(TAG, "onTouchEvent: deltaY:compareDelta" + deltaY + " : " + compareDelta);

                    if (onScrollRefreshListener != null && Math.abs(deltaY) > compareDelta && isScrollerInTop) {
                        /*Log.d(TAG, "onTouchEvent: SET CURRENT ITEM");*/
                        onScrollRefreshListener.onTopToBottomSwiped();
                        isScrollingLive = false;
                    }
                    isScrollingLive = false;
                case MotionEvent.ACTION_CANCEL:
                    isScrollingLive = false;
                    if (onScrollEndListener != null) {
                        startScrollerTask();
                    }
            }
            return super.onTouchEvent(ev);
        }
    }

    public boolean isScrollingLive() {
        return isScrollingLive;
    }

    private void initScroller() {
        scrollerTask = new Runnable() {
            public void run() {
                int newPosition = getScrollY();
                if (initialPosition - newPosition == 0) {//has stopped
                    if (onScrollEndListener != null) {
                        onScrollEndListener.onScrollEnded();
                    }
                } else {
                    initialPosition = getScrollY();
                    NestedRefreshView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public interface OnScrollDetectionListener {
        void onScrolledToTop();

        void onScrolledToBottom();
    }

    public interface OnScrollEndListener {
        void onScrollEnded();
    }

    public enum ScrollDetectDirection {
        TOP, BOTTOM, BOTH;
    }
}
