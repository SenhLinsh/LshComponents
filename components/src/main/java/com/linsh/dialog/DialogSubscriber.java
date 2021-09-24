package com.linsh.dialog;

import android.app.Activity;

import com.linsh.base.activity.ActivitySubscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/09/25
 *    desc   :
 * </pre>
 */
class DialogSubscriber implements ActivitySubscribe.OnDestroy {

    private final List<WeakReference<IDialog>> dialogs = new ArrayList<>();

    @Override
    public void attach(Activity activity) {
    }

    public void bind(IDialog dialog) {
        dialogs.add(new WeakReference<>(dialog));
    }

    public <T extends IDialog> T find(Class<T> dialogInterface) {
        return find(dialogInterface, false);
    }

    public <T extends IDialog> T find(Class<T> dialogInterface, boolean needShowing) {
        for (int i = 0; i < dialogs.size(); i++) {
            WeakReference<IDialog> reference = dialogs.get(i);
            IDialog dialog = reference.get();
            if (dialog == null) {
                dialogs.remove(i--);
                continue;
            }
            if (needShowing && !dialog.getDialog().isShowing()) {
                continue;
            }
            if (dialogInterface.isAssignableFrom(dialog.getClass())) {
                return (T) dialog;
            }
        }
        return null;
    }

    public IDialog findFirst() {
        return findFirst(false);
    }

    public IDialog findFirst(boolean needShowing) {
        for (int i = 0; i < dialogs.size(); i++) {
            WeakReference<IDialog> reference = dialogs.get(i);
            IDialog dialog = reference.get();
            if (dialog == null) {
                dialogs.remove(i--);
                continue;
            }
            if (needShowing && !dialog.getDialog().isShowing()) {
                continue;
            }
            return dialog;
        }
        return null;
    }

    public void dismissAll() {
        for (int i = 0; i < dialogs.size(); i++) {
            WeakReference<IDialog> reference = dialogs.get(i);
            IDialog dialog = reference.get();
            if (dialog == null) {
                dialogs.remove(i--);
                continue;
            }
            if (dialog.getDialog().isShowing()) {
                dialog.dismiss();
                dialogs.remove(i--);
            }
        }
    }

    @Override
    public void onDestroy() {
        dialogs.clear();
    }
}
