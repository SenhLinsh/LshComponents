package com.linsh.dialog.text;

import android.app.Dialog;

import com.linsh.dialog.DefaultDialogHelper;
import com.linsh.dialog.DialogHelper;
import com.linsh.dialog.custom.LshDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/15
 *    desc   :
 * </pre>
 */
abstract class LshDialogHelperImpl implements DefaultDialogHelper {

    protected final LshDialog dialog;

    public LshDialogHelperImpl(LshDialog.BaseDialogInterface lshDialog) {
        dialog = lshDialog.getDialog();
    }

    @Override
    public DefaultDialogHelper setTitle(CharSequence title) {
        dialog.setTitle(title);
        return this;
    }

    @Override
    public DefaultDialogHelper setPositiveButton() {
        setPositiveButton("确定", null);
        return this;
    }

    @Override
    public DefaultDialogHelper setPositiveButton(OnClickListener listener) {
        setPositiveButton(listener == null ? null : "确定", listener);
        return this;
    }

    @Override
    public DefaultDialogHelper setPositiveButton(CharSequence text, final OnClickListener listener) {
        if (text == null && listener == null) {
            dialog.setPositiveButton(null, null);
        } else {
            dialog.setPositiveButton(text == null ? "确定" : text.toString(),
                    listener == null ? null : new LshDialog.OnPositiveListener() {
                        @Override
                        public void onClick(LshDialog dialog) {
                            listener.onClick(LshDialogHelperImpl.this);
                        }
                    });
        }
        return this;
    }

    @Override
    public DefaultDialogHelper setNegativeButton() {
        setNegativeButton("取消", null);
        return this;
    }

    @Override
    public DefaultDialogHelper setNegativeButton(OnClickListener listener) {
        setNegativeButton(listener == null ? null : "取消", listener);
        return this;
    }

    @Override
    public DefaultDialogHelper setNegativeButton(CharSequence text, final OnClickListener listener) {
        if (text == null && listener == null) {
            dialog.setNegativeButton(null, null);
        } else {
            dialog.setNegativeButton(text == null ? "取消" : text.toString(),
                    listener == null ? null : new LshDialog.OnNegativeListener() {
                        @Override
                        public void onClick(LshDialog dialog) {
                            listener.onClick(LshDialogHelperImpl.this);
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
    public DialogHelper show() {
        dialog.show();
        return this;
    }

    @Override
    public DialogHelper dismiss() {
        dialog.dismiss();
        return this;
    }
}
