package com.linsh.dialog.text;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsh.dialog.DefaultDialogHelper;
import com.linsh.dialog.DialogHelper;
import com.linsh.dialog.custom.LshColorDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class LshColorTextDialogHelperImpl implements TextDialogHelper {

    private final LshColorDialog dialog;

    public LshColorTextDialogHelperImpl(Context context) {
        dialog = new LshColorDialog(context)
                .buildText()
                .show();
    }

    @Override
    public void setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            ((TextView) contentView).setText(text);
        }
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

    @Override
    public DefaultDialogHelper setTitle(CharSequence title) {

        return null;
    }

    @Override
    public DefaultDialogHelper setPositiveButton() {
        return null;
    }

    @Override
    public DefaultDialogHelper setPositiveButton(OnClickListener listener) {
        return null;
    }

    @Override
    public DefaultDialogHelper setPositiveButton(CharSequence text, OnClickListener listener) {
        return null;
    }

    @Override
    public DefaultDialogHelper setNegativeButton() {
        return null;
    }

    @Override
    public DefaultDialogHelper setNegativeButton(OnClickListener listener) {
        return null;
    }

    @Override
    public DefaultDialogHelper setNegativeButton(CharSequence text, OnClickListener listener) {
        return null;
    }
}
