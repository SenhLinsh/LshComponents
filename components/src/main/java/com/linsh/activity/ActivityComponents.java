package com.linsh.activity;

import android.content.Context;

import com.linsh.activity.impl.IActivityBuilderImpl;
import com.linsh.activity.impl.Register;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   : Activity 组件
 *
 *             对于常用的具有可复用或统一风格的页面, 我们将对其进行抽象化, 通过 ActivityComponents 进行注册
 *             和使用
 *
 *             1. IActivityFuture 进行参数传递相关接口的声明
 *             2. IActivity 进行 Activity 需要进行实现的接口声明
 * </pre>
 */
public class ActivityComponents {

    private static final Map<Class<? extends IActivity<?>>, Class<? extends IActivity.View>> IMPLEMENTS = new HashMap<>();

    static {
        // TODO: 2021/1/16 后期应该改为使用类注解, 并在编译期进行注册
        Register.init();
    }

    /**
     * 创建 IActivity.Builder, 进行 IActivity 构建
     */
    public static <T extends IActivity.Presenter> IActivity.Builder<T> create(Context context, Class<? extends IActivity<T>> iActivity) {
        if (!iActivity.isInterface())
            throw new IllegalArgumentException("iActivity must be interface");
        Class<? extends IActivity.View> iView = IMPLEMENTS.get(iActivity);
        if (iView == null)
            throw new IllegalStateException("no implement of IActivity: " + iActivity.getName());
        return new IActivityBuilderImpl<>(context, iView);
    }

    /**
     * 注册 IActivity
     *
     * @param iActivity 注册所需声明的 IActivity
     * @param iView     IActivity 页面构建所需 Contract.View 的实现
     */
    public static void register(Class<? extends IActivity<?>> iActivity, Class<? extends IActivity.View> iView) {
        if (!iActivity.isInterface())
            throw new IllegalArgumentException("iActivity must be interface");
        if (iView.isInterface())
            throw new IllegalArgumentException("iView can not be interface");
        IMPLEMENTS.put(iActivity, iView);
    }
}
