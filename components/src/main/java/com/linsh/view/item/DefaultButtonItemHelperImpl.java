package com.linsh.view.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/13
 *    desc   :
 * </pre>
 */
public class DefaultButtonItemHelperImpl implements ButtonItemHelper {

    private final Button button;

    public DefaultButtonItemHelperImpl(Context context) {
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
