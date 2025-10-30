package com.watutz.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class OnScrollObserver extends ScrollView {
    public interface OnScrollListener {
        public void onScrollChanged(OnScrollObserver scrollView, int x, int y, int oldX, int oldY);
        public void onEndScroll(OnScrollObserver scrollView);
    }

    private boolean mIsScrolling;
    private boolean mIsTouching;
    private Runnable mScrollingRunnable;
    private OnScrollListener mOnScrollListener;

    public OnScrollObserver(Context context) {
        this(context, null, 0);
    }

    public OnScrollObserver(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnScrollObserver(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            mIsTouching = true;
            mIsScrolling = true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (mIsTouching && !mIsScrolling) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onEndScroll(this);
                }
            }

            mIsTouching = false;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);

        if (Math.abs(oldX - x) > 0) {
            if (mScrollingRunnable != null) {
                removeCallbacks(mScrollingRunnable);
            }

            mScrollingRunnable = new Runnable() {
                public void run() {
                    if (mIsScrolling && !mIsTouching) {
                        if (mOnScrollListener != null) {
                            mOnScrollListener.onEndScroll(OnScrollObserver.this);
                        }
                    }

                    mIsScrolling = false;
                    mScrollingRunnable = null;
                }
            };

            postDelayed(mScrollingRunnable, 200);
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnEndScrollListener) {
        this.mOnScrollListener = mOnEndScrollListener;
    }

}