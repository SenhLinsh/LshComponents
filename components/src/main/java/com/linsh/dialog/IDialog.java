package com.linsh.dialog;

import android.app.Dialog;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public interface IDialog {

    default <T extends IDialog> T as(Class<T> classOfDialog) {
        return (T) this;
    }

    Dialog getDialog();

    IDialog show();

    IDialog dismiss();

    interface OnClickListener {
        void onClick(IDialog dialog);
    }

    interface OnItemClickListener {
        void onItemClick(@NonNull IDialog dialog, int position);
    }

    interface OnItemClickListenerEx<T> {
        void onItemClick(@NonNull IDialog dialog, T item, int position);
    }
}
