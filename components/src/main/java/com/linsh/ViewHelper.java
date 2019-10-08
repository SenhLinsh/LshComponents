package com.linsh;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.linsh.IViewHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   : View 管理辅助类
 * </pre>
 */
public class ViewHelper<T extends View> implements IViewHelper<T> {

    protected T mView;

    public ViewHelper(T view) {
        mView = view;
    }

    @Override
    public T getView() {
        return mView;
    }

    @Override
    public void setWidth(int width) {
        mView.getLayoutParams().width = width;
    }

    @Override
    public void setHeight(int height) {
        mView.getLayoutParams().height = height;
    }

    @Override
    public void setSelected(boolean selected) {
        mView.setSelected(selected);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mView.setEnabled(enabled);
    }

    @Override
    public void setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackground(drawable);
        } else {
            mView.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setBackground(int color) {
        mView.setBackgroundColor(color);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mView.setPadding(left, top, right, bottom);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }

}
