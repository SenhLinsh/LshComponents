package com.linsh.view;

import android.content.Context;

import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.impl.Register;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   :
 * </pre>
 */
public class ViewComponents {

    private static final Map<Class<? extends IView>, Class<? extends IView>> IMPLEMENTS = new HashMap<>();

    static {
        Register.init();
    }

    @NonNull
    public static <T extends IView> T create(final Context context, Class<T> viewHelper) {
        if (!viewHelper.isInterface())
            throw new IllegalArgumentException("viewHelper must be interface");
        Class<? extends IView> clazz = IMPLEMENTS.get(viewHelper);
        try {
            Object instance = ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context});
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Class<? extends IView>... classes) {
        for (Class<? extends IView> clazz : classes) {
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register interface");
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> interfaceClass : interfaces) {
                    while (interfaceClass != null
                            && interfaceClass != IView.class
                            && interfaceClass != Object.class
                            && IView.class.isAssignableFrom(interfaceClass)) {
                        IMPLEMENTS.put((Class<? extends IView>) interfaceClass, clazz);
                        interfaceClass = interfaceClass.getSuperclass();
                    }
                }
            }
        }
    }
}
