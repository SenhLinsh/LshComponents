package com.linsh.dialog.impl.alert;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;

import com.linsh.dialog.text.IInputDialog;
import com.linsh.lshutils.utils.Dps;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/04/18
 *    desc   :
 * </pre>
 */
public class InputAlertDialogImpl extends BaseAlertDialogImpl implements IInputDialog {

    private final EditText editText;

    public InputAlertDialogImpl(Context context) {
        super(context);
        editText = new EditText(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                100, 100);
        dialog.setView(editText, Dps.toPx(20), 0, Dps.toPx(20), 0);
    }

    @Override
    public IInputDialog setText(CharSequence text) {
        editText.setText(text);
        return this;
    }

    @Override
    public CharSequence getText() {
        return editText.getText();
    }
}
