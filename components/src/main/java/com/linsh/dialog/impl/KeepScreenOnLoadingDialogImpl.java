package com.linsh.dialog.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.linsh.dialog.IDialog;
import com.linsh.dialog.custom.LshDialog;
import com.linsh.dialog.loading.IKeepScreenOnLoadingDialog;
import com.linsh.dialog.loading.IProgressLoadingDialog;
import com.linsh.dialog.loading.ITextLoadingDialog;
import com.linsh.lshutils.utils.ScreenUtilsEx;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/03/21
 *    desc   :
 * </pre>
 */
class KeepScreenOnLoadingDialogImpl extends LshDialogImpl implements IKeepScreenOnLoadingDialog {

    private boolean keepScreenOn;

    public KeepScreenOnLoadingDialogImpl(Context context) {
        super(new LshDialog(context).buildText());
        setText("加载中...");
    }

    @Override
    public IKeepScreenOnLoadingDialog keepScreenOn(boolean enable) {
        keepScreenOn = enable;
        Window window = dialog.getWindow();
        if (window != null) {
            ScreenUtilsEx.keepScreenOn(window);
        }
        return this;
    }

    @Override
    public IProgressLoadingDialog setProgress(CharSequence text) {
        setText("加载中...\n当前进度：" + text);
        return this;
    }

    @Override
    public IDialog show() {
        IDialog show = super.show();
        show.getDialog().setCanceledOnTouchOutside(false);
        show.getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface ignore) {
                if (keepScreenOn) {
                    Window window = dialog.getWindow();
                    if (window != null) {
                        ScreenUtilsEx.keepScreenOn(window);
                    }
                }
            }
        });
        show.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface ignore) {
                if (keepScreenOn) {
                    Window window = dialog.getWindow();
                    if (window != null) {
                        ScreenUtilsEx.clearKeepScreenOn(window);
                    }
                }
            }
        });
        return show;
    }

    @Override
    public ITextLoadingDialog setText(CharSequence text) {
        View contentView = dialog.getContentView();
        if (contentView instanceof TextView) {
            ((TextView) contentView).setText(text);
        }
        return this;
    }
}
