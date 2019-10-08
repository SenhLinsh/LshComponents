package com.linsh.views.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linsh.TextViewHelper;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class TextPreference extends Preference<TextViewHelper> {


    public TextPreference(Context context) {
        super(context);
    }

    public TextPreference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TextViewHelper initDetailHelper() {
        TextView detail = new TextView(getContext());
        TextViewHelper detailHelper = new TextViewHelper(detail);
        return detailHelper;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextPreference);
        String detailText = typedArray.getString(R.styleable.TextPreference_detailText);
        int detailTextColor = typedArray.getInt(R.styleable.TextPreference_detailTextColor, 0);
        int detailTextSize = typedArray.getDimensionPixelSize(R.styleable.TextPreference_detailTextSize, -1);

        TextViewHelper detail = detail();
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
