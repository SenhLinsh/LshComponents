package com.linsh.dialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public interface DefaultDialogHelper extends DialogHelper {

    DefaultDialogHelper setTitle(CharSequence title);

    DefaultDialogHelper setPositiveButton();

    DefaultDialogHelper setPositiveButton(OnClickListener listener);

    DefaultDialogHelper setPositiveButton(CharSequence text, OnClickListener listener);

    DefaultDialogHelper setNegativeButton();

    DefaultDialogHelper setNegativeButton(OnClickListener listener);

    DefaultDialogHelper setNegativeButton(CharSequence text, OnClickListener listener);

}
