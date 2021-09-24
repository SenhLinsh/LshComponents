package com.linsh.dialog;

import android.content.Context;

import com.linsh.base.LshLog;
import com.linsh.base.activity.IObservableActivity;
import com.linsh.dialog.impl.Register;
import com.linsh.utilseverywhere.ClassUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/14
 *    desc   :
 * </pre>
 */
public class DialogComponents {

    private static final String TAG = "DialogComponents";
    private static final Map<Class<? extends IDialog>, Class<? extends IDialog>> IMPLEMENTS = new HashMap<>();

    static {
        Register.init();
    }

    private DialogComponents() {
    }

    @NonNull
    public static <T extends IDialog> T create(final Context context, Class<T> dialogHelper) {
        if (!dialogHelper.isInterface())
            throw new IllegalArgumentException("viewHelper must be interface");
        Class<? extends IDialog> clazz = IMPLEMENTS.get(dialogHelper);
        if (clazz == null) {
            throw new RuntimeException("can not find implement for dialog helper: " + dialogHelper.getName());
        }
        try {
            IDialog dialog = (IDialog) ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context}, true);
            if (context instanceof IObservableActivity) {
                ((IObservableActivity) context).subscribe(DialogSubscriber.class).bind(dialog);
            }
            return (T) dialog;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T extends IDialog> T find(Context context, Class<T> dialogClass) {
        if (context instanceof IObservableActivity) {
            return ((IObservableActivity) context).subscribe(DialogSubscriber.class).find(dialogClass);
        }
        return null;
    }

    public static void dismissAll(Context context) {
        if (context instanceof IObservableActivity) {
            ((IObservableActivity) context).subscribe(DialogSubscriber.class).dismissAll();
        }
    }

    public static void register(Class<? extends IDialog>... classes) {
        for (Class<? extends IDialog> implementClass : classes) {
            Class<?> clazz = implementClass;
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register Interface class");
            }
            while (clazz != null && IDialog.class.isAssignableFrom(clazz)) {
                registerInterface(implementClass, clazz.getInterfaces());
                clazz = clazz.getSuperclass();
            }
        }
    }

    private static void registerInterface(Class<? extends IDialog> clazz, Class<?>[] interfaces) {
        if (interfaces != null) {
            for (Class<?> interfaceClass : interfaces) {
                if (interfaceClass != null
                        && IDialog.class.isAssignableFrom(interfaceClass)
                        && interfaceClass != IDefaultDialog.class
                        && interfaceClass != IDialog.class) {
                    IMPLEMENTS.put((Class<? extends IDialog>) interfaceClass, clazz);
                    LshLog.d(TAG, "register implement for " + interfaceClass.getName() + ": " + clazz.getName());
                    registerInterface(clazz, interfaceClass.getInterfaces());
                }
            }
        }
    }
}
