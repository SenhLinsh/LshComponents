package com.linsh.dialog;

import android.app.Dialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public interface DialogHelper {

    Dialog getDialog();

    DialogHelper show();

    DialogHelper dismiss();

    interface OnClickListener {
        void onClick(DialogHelper dialogHelper);
    }

    interface OnItemClickListener {
        void onItemClick(DialogHelper dialogHelper, int position);
    }
}
