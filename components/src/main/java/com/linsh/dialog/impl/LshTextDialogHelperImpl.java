package com.linsh.dialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.text.TextDialogHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class LshTextDialogHelperImpl extends LshDialogHelperImpl implements TextDialogHelper {

    public LshTextDialogHelperImpl(Context context) {
        super(new LshDialog(context).buildText());
    }

    @Override
    public TextDialogHelper setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            ((TextView) contentView).setText(text);
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
