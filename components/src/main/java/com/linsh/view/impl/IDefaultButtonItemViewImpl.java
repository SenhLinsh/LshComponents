package com.linsh.view.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linsh.view.item.IButtonItemView;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/13
 *    desc   :
 * </pre>
 */
class IDefaultButtonItemViewImpl implements IButtonItemView {

    private final Button button;

    public IDefaultButtonItemViewImpl(Context context) {
        button = new Button(context);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setText(CharSequence text) {
        button.setText(text);
    }

    @NonNull
    @Override
    public View getView() {
        return button;
    }
}
