package com.linsh.views.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 序列圆形进度条, 多个进度条组合进行拼接成一个
 * </pre>
 */
public class SeriesCircleProgress extends BaseCircleProgress {


    public SeriesCircleProgress(Context context) {
        super(context);
    }

    public SeriesCircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeriesCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        super.init();
        if (isInEditMode()) {
            setProgressWidth(20);
            setProgress(new int[]{40, 30, 20});
            setProgressColor(new int[]{Color.RED, Color.BLUE, Color.YELLOW});

            setEmptyColor(0x110000FF);
        }
    }

    @Override
    public void setProgress(int[] progress) {
        super.setProgress(progress);
    }

    @Override
    public void setProgressColor(int[] colors) {
        super.setProgressColor(colors);
    }

    @Override
    public void setProgressWithAnimation(int[] progresses) {
        super.setProgressWithAnimation(progresses);
    }

    @Override
    public void setProgressWithAnimation(int[] progresses, long duration) {
        super.setProgressWithAnimation(progresses, duration);
    }

    @Override
    protected void onDrawProgress(Canvas canvas, float startAngle, float endAngle) {
        float sweepAngle;
        for (int i = 0; i < mCurrentProgresses.length; i++) {
            sweepAngle = mCurrentProgresses[i] * 3.6f;
            if (mProgressColors != null && i < mProgressColors.length) {
                mPaint.setColor(mProgressColors[i]);
            } else {
                mPaint.setColor(DEFAULT_COLOR);
            }
            canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
            startAngle += sweepAngle;
        }
        if (Math.round(startAngle) < endAngle) {
            mPaint.setColor(mEmptyColor);
            canvas.drawArc(mRectF, startAngle, endAngle - startAngle, false, mPaint);
        }
    }
}
