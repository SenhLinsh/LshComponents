package com.linsh.view.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linsh.utilseverywhere.UnitConverseUtils;
import com.linsh.views.R;

import androidx.annotation.Nullable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/08
 *    desc   :
 * </pre>
 */
public abstract class Preference<T extends View> extends LinearLayout {

    private TextView mTitleHelper;
    private T mDetailHelper;

    public Preference(Context context) {
        super(context);
        init(null);
    }

    public Preference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        initPreference();
        mTitleHelper = initTitleHelper();
        mDetailHelper = initDetailHelper();

        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.leftMargin = UnitConverseUtils.dp2px(10);
        params2.gravity = Gravity.RIGHT;
        addView(mTitleHelper, params0);
        FrameLayout frameLayout = new FrameLayout(getContext());
        addView(frameLayout, params1);
        frameLayout.addView(mDetailHelper, params2);
        initAttr(attrs);
    }

    protected void initPreference() {
        setMinimumHeight(dp2px(45));
        int dp10 = UnitConverseUtils.dp2px(10);
        setPadding(dp10, dp10, dp10, dp10);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    protected TextView initTitleHelper() {
        TextView title = new TextView(getContext());
        title.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        return title;
    }

    protected abstract T initDetailHelper();

    protected void initAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Preference);
            int titleWidth = typedArray.getDimensionPixelSize(R.styleable.Preference_titleWidth, -1);
            int titleHeight = typedArray.getDimensionPixelSize(R.styleable.Preference_titleHeight, -1);
            int detailWidth = typedArray.getDimensionPixelSize(R.styleable.Preference_detailWidth, -1);
            int detailHeight = typedArray.getDimensionPixelSize(R.styleable.Preference_detailHeight, -1);
            String titleText = typedArray.getString(R.styleable.Preference_titleText);
            int titleTextColor = typedArray.getColor(R.styleable.Preference_titleTextColor, 0);
            int titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Preference_titleTextSize, -1);

            if (titleWidth >= 0) {
                mTitleHelper.setWidth(titleWidth);
            }
            if (titleHeight >= 0) {
                mTitleHelper.setHeight(titleHeight);
            }
            if (detailWidth >= 0) {
                mDetailHelper.getLayoutParams().width = detailWidth;
            }
            if (detailHeight >= 0) {
                mDetailHelper.getLayoutParams().height = detailHeight;
            }
            if (titleText != null) {
                mTitleHelper.setText(titleText);
            }
            if (titleTextColor != 0) {
                mTitleHelper.setTextColor(titleTextColor);
            }
            if (titleTextSize > 0) {
                mTitleHelper.setTextSize(titleTextSize);
            }
            typedArray.recycle();
        }
    }

    public TextView title() {
        return mTitleHelper;
    }

    public T detail() {
        return mDetailHelper;
    }

    protected int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
