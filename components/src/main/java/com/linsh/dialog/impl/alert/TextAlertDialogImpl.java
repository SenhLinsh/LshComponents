package com.linsh.dialog.impl.alert;

import android.content.Context;

import com.linsh.dialog.text.ITextDialog;
import com.linsh.utilseverywhere.StringUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class TextAlertDialogImpl extends BaseAlertDialogImpl implements ITextDialog {

    private CharSequence text = "";

    public TextAlertDialogImpl(Context context) {
        super(context);
    }

    @Override
    public ITextDialog setText(CharSequence text) {
        dialog.setMessage(this.text = StringUtils.nullToEmpty(text));
        return this;
    }

    @Override
    public CharSequence getText() {
        return text;
    }
}
