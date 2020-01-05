package com.linsh.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.text.InputDialogHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class LshInputDialogHelperImpl extends LshDialogHelperImpl implements InputDialogHelper {

    public LshInputDialogHelperImpl(Context context) {
        super(new LshDialog(context).buildInput());
    }

    @Override
    public InputDialogHelper setText(CharSequence text) {
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
