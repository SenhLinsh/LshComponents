package com.linsh.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.linsh.base.LshLog;
import com.linsh.base.activity.ActivitySubscribe;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/09/25
 *    desc   :
 * </pre>
 */
class DialogSubscriber implements ActivitySubscribe.OnDestroy {

    private static final String TAG = "DialogSubscriber";
    private final Map<Dialog, IDialog> dialogs = new WeakHashMap<>();

    @Override
    public void attach(Activity activity) {
    }

    public void bind(IDialog dialog) {
        LshLog.i(TAG, "bind dialog, implement: " + dialog.getClass().getSimpleName());
        dialogs.put(dialog.getDialog(), dialog);
    }

    public <T extends IDialog> T find(Class<T> dialogInterface) {
        return find(dialogInterface, false);
    }

    public <T extends IDialog> T find(Class<T> dialogInterface, boolean needShowing) {
        Iterator<Map.Entry<Dialog, IDialog>> iterator = dialogs.entrySet().iterator();
        while (iterator.hasNext()) {
            IDialog dialog = iterator.next().getValue();
            if (dialog == null) {
                iterator.remove();
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
        Iterator<Map.Entry<Dialog, IDialog>> iterator = dialogs.entrySet().iterator();
        while (iterator.hasNext()) {
            IDialog dialog = iterator.next().getValue();
            if (dialog == null) {
                iterator.remove();
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
        LshLog.i(TAG, "dismiss all");
        Iterator<Map.Entry<Dialog, IDialog>> iterator = dialogs.entrySet().iterator();
        while (iterator.hasNext()) {
            IDialog dialog = iterator.next().getValue();
            if (dialog == null) {
                LshLog.d(TAG, "find empty dialog");
                iterator.remove();
                continue;
            }
            if (dialog.getDialog().isShowing()) {
                LshLog.d(TAG, "find showing dialog: " + dialog.getClass().getSimpleName() + ", dismiss it");
                dialog.dismiss();
                iterator.remove();
            } else {
                LshLog.d(TAG, "find dismissed dialog: " + dialog.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void onDestroy() {
        dialogs.clear();
    }
}
