package com.linsh.dialog.impl.alert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.linsh.dialog.IDefaultDialog;
import com.linsh.dialog.IDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/04/18
 *    desc   :
 * </pre>
 */
class BaseAlertDialogImpl implements IDefaultDialog {

    protected final AlertDialog dialog;

    public BaseAlertDialogImpl(Context context) {
        dialog = new AlertDialog.Builder(context)
                .create();
    }

    @Override
    public IDefaultDialog setTitle(CharSequence title) {
        dialog.setTitle(title);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton() {
        setPositiveButton(null, null);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton(OnClickListener listener) {
        setPositiveButton(null, listener);
        return this;
    }

    @Override
    public IDefaultDialog setPositiveButton(CharSequence text, OnClickListener listener) {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, text == null ? "确定" : text, (dialog, which) -> {
            if (listener != null) {
                listener.onClick(BaseAlertDialogImpl.this);
            }
        });
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton() {
        setPositiveButton(null, null);
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton(OnClickListener listener) {
        setPositiveButton(null, listener);
        return this;
    }

    @Override
    public IDefaultDialog setNegativeButton(CharSequence text, OnClickListener listener) {
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, text == null ? "取消" : text, (dialog, which) -> {
            if (listener != null) {
                listener.onClick(BaseAlertDialogImpl.this);
            }
        });
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
