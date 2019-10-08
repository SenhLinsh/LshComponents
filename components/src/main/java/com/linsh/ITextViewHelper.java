package com.linsh;

import android.widget.TextView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/13
 *    desc   : ViewHelper 接口
 * </pre>
 */
public interface ITextViewHelper extends IViewHelper<TextView> {

    CharSequence getText();

    void setText(CharSequence text);

    void setTextColor(int color);

    void setTextSize(float size);

    void setHint(CharSequence hint);
}
