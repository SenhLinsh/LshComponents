package com.linsh.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.loading.TextLoadingDialogHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class LshTextLoadingDialogHelperImpl extends LshDialogHelperImpl implements TextLoadingDialogHelper {

    public LshTextLoadingDialogHelperImpl(Context context) {
        super(new LshDialog(context).buildText().setContent("加载中..."));
    }

    @Override
    public TextLoadingDialogHelper setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            ((TextView) contentView).setText(text);
        }
        return this;
    }
}
