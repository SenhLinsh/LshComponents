package com.linsh;

import android.widget.TextView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/13
 *    desc   :
 * </pre>
 */
public class TextViewHelper extends ViewHelper<TextView> implements ITextViewHelper {

    public TextViewHelper(TextView view) {
        super(view);
    }

    @Override
    public void setText(CharSequence text) {
        mView.setText(text);
    }

    @Override
    public CharSequence getText() {
        return mView.getText();
    }

    @Override
    public void setTextColor(int color) {
        mView.setTextColor(color);
    }

    @Override
    public void setTextSize(float size) {
        mView.setTextSize(size);
    }

    @Override
    public void setHint(CharSequence hint) {
        mView.setHint(hint);
    }
}
