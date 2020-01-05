package com.linsh.dialog;

import android.content.Context;

import com.linsh.base.LshLog;
import com.linsh.dialog.impl.LshInputDialogHelperImpl;
import com.linsh.dialog.impl.LshTextDialogHelperImpl;
import com.linsh.dialog.impl.LshTextLoadingDialogHelperImpl;
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
    private static final Map<Class<? extends DialogHelper>, Class<? extends DialogHelper>> IMPLEMENTS = new HashMap<>();

    static {
        register(
                LshInputDialogHelperImpl.class,
                LshTextDialogHelperImpl.class,
                LshTextLoadingDialogHelperImpl.class
        );
    }

    private DialogComponents() {
    }

    @NonNull
    public static <T extends DialogHelper> T create(final Context context, Class<T> dialogHelper) {
        if (!dialogHelper.isInterface())
            throw new IllegalArgumentException("viewHelper must be interface");
        Class<? extends DialogHelper> clazz = IMPLEMENTS.get(dialogHelper);
        if (clazz == null) {
            throw new RuntimeException("can not find implement for dialog helper: " + dialogHelper.getName());
        }
        try {
            Object instance = ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context});
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Class<? extends DialogHelper>... classes) {
        for (Class<? extends DialogHelper> implementClass : classes) {
            Class<?> clazz = implementClass;
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register Interface class");
            }
            while (clazz != null && DialogHelper.class.isAssignableFrom(clazz)) {
                registerInterface(implementClass, clazz.getInterfaces());
                clazz = clazz.getSuperclass();
            }
        }
    }

    private static void registerInterface(Class<? extends DialogHelper> clazz, Class<?>[] interfaces) {
        if (interfaces != null) {
            for (Class<?> interfaceClass : interfaces) {
                if (interfaceClass != null
                        && DialogHelper.class.isAssignableFrom(interfaceClass)
                        && interfaceClass != DefaultDialogHelper.class
                        && interfaceClass != DialogHelper.class) {
                    IMPLEMENTS.put((Class<? extends DialogHelper>) interfaceClass, clazz);
                    LshLog.d(TAG, "register implement for " + interfaceClass.getName() + ": " + clazz.getName());
                    registerInterface(clazz, interfaceClass.getInterfaces());
                }
            }
        }
    }
}
