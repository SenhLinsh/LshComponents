package com.linsh.dialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public interface IDefaultDialog extends IDialog {

    IDefaultDialog setTitle(CharSequence title);

    IDefaultDialog setPositiveButton();

    IDefaultDialog setPositiveButton(OnClickListener listener);

    IDefaultDialog setPositiveButton(CharSequence text, OnClickListener listener);

    IDefaultDialog setNegativeButton();

    IDefaultDialog setNegativeButton(OnClickListener listener);

    IDefaultDialog setNegativeButton(CharSequence text, OnClickListener listener);

}
