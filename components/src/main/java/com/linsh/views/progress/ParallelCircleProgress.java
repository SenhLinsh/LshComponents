package com.linsh.views.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 并行圆形进度条, 多个进度条并列显示(向圆心扩展)
 * </pre>
 */
public class ParallelCircleProgress extends BaseCircleProgress {

    private RectF mCurRectF = new RectF();

    public ParallelCircleProgress(Context context) {
        super(context);
        init();
    }

    public ParallelCircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallelCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        super.init();

        if (isInEditMode()) {
            setProgressWidth(20);
            setProgress(new int[]{50, 30, 20});
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
        mCurRectF.left = mRectF.left;
        mCurRectF.right = mRectF.right;
        mCurRectF.top = mRectF.top;
        mCurRectF.bottom = mRectF.bottom;

        for (int i = 0; i < mCurrentProgresses.length; i++) {
            sweepAngle = mCurrentProgresses[i] * 3.6f;
            if (mProgressColors != null && i < mProgressColors.length) {
                mPaint.setColor(mProgressColors[i]);
            } else {
                mPaint.setColor(DEFAULT_COLOR);
            }
            canvas.drawArc(mCurRectF, startAngle, sweepAngle, false, mPaint);

            if (Math.round(sweepAngle) < endAngle) {
                mPaint.setColor(mEmptyColor);
                canvas.drawArc(mCurRectF, startAngle + sweepAngle, endAngle - startAngle - sweepAngle, false, mPaint);
            }

            mCurRectF.left += mStrokeWidth;
            mCurRectF.right -= mStrokeWidth;
            mCurRectF.top += mStrokeWidth;
            mCurRectF.bottom -= mStrokeWidth;
        }
    }
}
