package com.linsh.dialog.impl;

import android.app.Dialog;

import com.linsh.dialog.IDefaultDialog;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.custom.LshDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/15
 *    desc   :
 * </pre>
 */
abstract class LshDialogImpl implements IDefaultDialog {

    protected final LshDialog dialog;

    public LshDialogImpl(LshDialog.BaseDialogInterface lshDialog) {
        dialog = lshDialog.getDialog();
    }

    @Override
    public IDefaultDialog setTitle(CharSequence title) {
        dialog.setTitle(title);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton() {
        setPositiveButton("确定", null);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton(OnClickListener listener) {
        setPositiveButton(listener == null ? null : "确定", listener);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton(CharSequence text, final OnClickListener listener) {
        if (text == null && listener == null) {
            dialog.setPositiveButton(null, null);
        } else {
            dialog.setPositiveButton(text == null ? "确定" : text.toString(),
                    listener == null ? null : new LshDialog.OnPositiveListener() {
                        @Override
                        public void onClick(LshDialog dialog) {
                            listener.onClick(LshDialogImpl.this);
                        }
                    });
        }
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton() {
        setNegativeButton("取消", null);
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton(OnClickListener listener) {
        setNegativeButton(listener == null ? null : "取消", listener);
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton(CharSequence text, final OnClickListener listener) {
        if (text == null && listener == null) {
            dialog.setNegativeButton(null, null);
        } else {
            dialog.setNegativeButton(text == null ? "取消" : text.toString(),
                    listener == null ? null : new LshDialog.OnNegativeListener() {
                        @Override
                        public void onClick(LshDialog dialog) {
                            listener.onClick(LshDialogImpl.this);
                        }
                    });
        }
        return this;
    }

    @Override
    public Dialog getDialog() {
        return dialog;
    }

    @Override
    public IDialog show() {
        dialog.show();
        return this;
    }

    @Override
    public IDialog dismiss() {
        dialog.dismiss();
        return this;
    }
}
