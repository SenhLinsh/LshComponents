package com.linsh.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.text.IInputDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
class LshInputDialogImpl extends LshDialogImpl implements IInputDialog {

    public LshInputDialogImpl(Context context) {
        super(new LshDialog(context).buildInput());
    }

    @Override
    public IInputDialog setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof EditText) {
            ((EditText) contentView).setText(text);
            ((EditText) contentView).setSelection(text.length());
        }
        return this;
    }

    @Override
    public CharSequence getText() {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            return ((TextView) contentView).getText();
        }
        return null;
    }
}
