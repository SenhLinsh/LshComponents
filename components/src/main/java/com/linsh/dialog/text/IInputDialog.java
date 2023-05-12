package com.linsh.dialog.text;

import com.linsh.dialog.IDefaultDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public interface IInputDialog extends IDefaultDialog {

    IInputDialog setText(CharSequence text);

    CharSequence getText();

    IInputDialog setHint(CharSequence hint);

    IInputDialog showKeyboard(boolean show);
}
