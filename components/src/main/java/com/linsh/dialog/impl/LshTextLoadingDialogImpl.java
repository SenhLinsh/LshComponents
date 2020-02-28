package com.linsh.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.loading.ITextLoadingDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
class LshTextLoadingDialogImpl extends LshDialogImpl implements ITextLoadingDialog {

    public LshTextLoadingDialogImpl(Context context) {
        super(new LshDialog(context).buildText().setContent("加载中..."));
    }

    @Override
    public ITextLoadingDialog setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            ((TextView) contentView).setText(text);
        }
        return this;
    }
}
