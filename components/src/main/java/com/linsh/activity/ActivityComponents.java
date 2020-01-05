package com.linsh.activity;

import android.content.Context;

import com.linsh.activity.impl.ManagerItemActivityStarterImpl;
import com.linsh.activity.impl.PhotoViewActivityStarterImpl;
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

    private static final Map<Class<? extends ActivityStarter>, Class<? extends ActivityStarter>> IMPLEMENTS = new HashMap<>();

    static {
        register(
                ManagerItemActivityStarterImpl.class,
                PhotoViewActivityStarterImpl.class
        );
    }

    @NonNull
    public static <T extends ActivityStarter> T create(final Context context, Class<T> activityHelper) {
        if (!activityHelper.isInterface())
            throw new IllegalArgumentException("activityHelper must be interface");
        Class<? extends ActivityStarter> clazz = IMPLEMENTS.get(activityHelper);
        try {
            Object instance = ClassUtils.newInstance(clazz, new Class[]{Context.class}, new Object[]{context});
            return (T) instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Class<? extends ActivityStarter>... classes) {
        for (Class<? extends ActivityStarter> clazz : classes) {
            if (clazz.isInterface()) {
                throw new IllegalArgumentException("can not register interface");
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> interfaceClass : interfaces) {
                    while (interfaceClass != null
                            && interfaceClass != ActivityStarter.class
                            && interfaceClass != Object.class
                            && ActivityStarter.class.isAssignableFrom(interfaceClass)) {
                        IMPLEMENTS.put((Class<? extends ActivityStarter>) interfaceClass, clazz);
                        interfaceClass = interfaceClass.getSuperclass();
                    }
                }
            }
        }
    }
}
