package com.watutz.app.view;

import com.watutz.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class SeekBarView extends SeekBar {

    private int mThumbSize;
    private TextPaint mTextPaint;
    private String metric = "km";

    public SeekBarView(Context context) {
        this(context, null);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        mThumbSize = 45;
        
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(15);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.SeekBarView, 0, 0);

	    // How high the content should be in "collapsed" state
	    metric = (String) a.getString(
	                R.styleable.SeekBarView_metric);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String progressText = String.valueOf(getProgress()) + metric;
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);

        int leftPadding = getPaddingLeft() - getThumbOffset();
        int rightPadding = getPaddingRight() - getThumbOffset();
        int width = getWidth() - leftPadding - rightPadding;
        float progressRatio = (float) getProgress() / getMax();
        float thumbOffset = mThumbSize * (.5f - progressRatio);
        float thumbX = progressRatio * width + leftPadding + thumbOffset;
        float thumbY = getHeight() / 2f + bounds.height() / 2f;
        canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
    }
}
