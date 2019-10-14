package com.linsh.dialog;

import android.content.Context;

import com.linsh.dialog.text.LshColorInputDialogHelperImpl;
import com.linsh.dialog.text.LshColorTextDialogHelperImpl;
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

    private static final Map<Class<? extends DialogHelper>, Class<? extends DialogHelper>> IMPLEMENTS = new HashMap<>();

    static {
        register(
                LshColorInputDialogHelperImpl.class,
                LshColorTextDialogHelperImpl.class
        );
    }

    private DialogComponents() {
    }

    @NonNull
    public static <T extends DialogHelper> T create(final Context context, Class<T> dialogHelper) {
        if (!dialogHelper.isInterface())
            throw new IllegalArgumentException("viewHelper must be interface");
        Class<? extends DialogHelper> clazz = IMPLEMENTS.get(dialogHelper);
        try {
            Object instance = ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context});
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void register(Class<? extends DialogHelper>... classes) {
        for (Class<? extends DialogHelper> clazz : classes) {
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register Interface class");
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> interfaceClass : interfaces) {
                    while (interfaceClass != null
                            && interfaceClass != DialogHelper.class
                            && interfaceClass != Object.class
                            && DialogHelper.class.isAssignableFrom(interfaceClass)) {
                        IMPLEMENTS.put((Class<? extends DialogHelper>) interfaceClass, clazz);
                        interfaceClass = interfaceClass.getSuperclass();
                    }
                }
            }
        }
    }
}
