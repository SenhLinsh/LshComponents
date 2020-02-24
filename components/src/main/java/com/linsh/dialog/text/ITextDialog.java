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
public interface ITextDialog extends IDefaultDialog {

    ITextDialog setText(CharSequence text);

    CharSequence getText();
}
