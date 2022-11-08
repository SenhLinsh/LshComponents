package com.linsh.dialog.impl.alert;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;

import com.linsh.dialog.text.IInputDialog;
import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.KeyboardUtils;

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
        editText.setSelection(text.length());
        return this;
    }

    @Override
    public CharSequence getText() {
        return editText.getText();
    }

    @Override
    public IInputDialog showKeyboard(boolean show) {
        if (show) {
            dialog.setOnShowListener(dialog -> HandlerUtils.postRunnable(() -> KeyboardUtils.showKeyboard(editText), 100));
        } else {
            dialog.setOnShowListener(null);
        }
        return this;
    }
}
