package com.linsh.dialog.text;

import com.linsh.dialog.IDefaultDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/01/20
 *    desc   :
 * </pre>
 */
public interface ISingleChoiceDialog extends IDefaultDialog {

    ISingleChoiceDialog addItem(CharSequence item, OnItemClickListener listener);
}
