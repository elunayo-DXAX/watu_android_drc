package com.watutz.app.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.watutz.app.R;


/**
 * Created by Marco on 03/03/15.
 */
public class TriangleTopRightGreen extends RelativeLayout {

    Paint paint= new Paint();
    Paint bgPaint= new Paint();

    public TriangleTopRightGreen(Context context) {
        super(context);
        init();
    }

    public TriangleTopRightGreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleTopRightGreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setColor(int theColor)
    {
         bgPaint.setColor(theColor);	
    }
    private void init() {

    	paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white));

        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(getResources().getColor(R.color.theme_green));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getMeasuredHeight();
        int w = getMeasuredWidth();

        canvas.drawRect(0,0,w,h,bgPaint);

        Path path = new Path();
        path.moveTo(0, h);
        path.lineTo(w, h);
        path.lineTo(0, 0);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path,paint);
    }
}