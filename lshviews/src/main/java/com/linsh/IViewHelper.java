package com.linsh;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/13
 *    desc   : ViewHelper 接口
 * </pre>
 */
public interface IViewHelper<T extends View> {

    T getView();

    void setWidth(int width);

    void setHeight(int height);

    void setSelected(boolean selected);

    void setEnabled(boolean enabled);

    void setBackground(Drawable drawable);

    void setBackground(int color);

    void setPadding(int left, int top, int right, int bottom);

    void setOnClickListener(View.OnClickListener listener);
}
