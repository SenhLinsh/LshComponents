package com.linsh.activity;

import android.content.Context;

import com.linsh.activity.impl.Register;
import com.linsh.utilseverywhere.ClassUtils;

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
public class ActivityComponents {

    private static final Map<Class<? extends ActivityFuture>, Class<? extends ActivityFuture>> IMPLEMENTS = new HashMap<>();

    static {
        Register.init();
    }

    @NonNull
    public static <T extends ActivityFuture> T create(final Context context, Class<T> activityHelper) {
        if (!activityHelper.isInterface())
            throw new IllegalArgumentException("activityHelper must be interface");
        Class<? extends ActivityFuture> clazz = IMPLEMENTS.get(activityHelper);
        try {
            Object instance = ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context}, true);
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Class<? extends ActivityFuture>... classes) {
        for (Class<? extends ActivityFuture> clazz : classes) {
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register interface");
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> interfaceClass : interfaces) {
                    while (interfaceClass != null
                            && interfaceClass != ActivityFuture.class
                            && interfaceClass != Object.class
                            && ActivityFuture.class.isAssignableFrom(interfaceClass)) {
                        IMPLEMENTS.put((Class<? extends ActivityFuture>) interfaceClass, clazz);
                        interfaceClass = interfaceClass.getSuperclass();
                    }
                }
            }
        }
    }
}
