package com.linsh.view.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linsh.components.R;

import androidx.annotation.Nullable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class TextPreference extends Preference<TextView> {


    public TextPreference(Context context) {
        super(context);
    }

    public TextPreference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TextView initDetailHelper() {
        TextView detail = new TextView(getContext());
        return detail;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextPreference);
        String detailText = typedArray.getString(R.styleable.TextPreference_detailText);
        int detailTextColor = typedArray.getInt(R.styleable.TextPreference_detailTextColor, 0);
        int detailTextSize = typedArray.getDimensionPixelSize(R.styleable.TextPreference_detailTextSize, -1);

        TextView detail = detail();
        if (detailText != null) {
            detail.setText(detailText);
        }
        if (detailTextColor != 0) {
            detail.setTextColor(detailTextColor);
        }
        if (detailTextSize > 0) {
            detail.setTextSize(detailTextSize);
        }
        typedArray.recycle();
    }
}
