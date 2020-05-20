package com.linsh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.base.activity.IntentDelegate;
import com.linsh.base.mvp.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/24
 *    desc   : 用于声明 Activity 页面类型的接口
 *
 *             不同的 IActivity 定义接口来进行页面区分, 不同的 IActivity 实现也可以得到不同的页面风格
 * </pre>
 */
public interface IActivity<P extends IActivity.Presenter> {

    /**
     * 用于自定义 IActivity 业务逻辑
     */
    interface Presenter<T extends IActivity.View> extends Contract.Presenter<T> {
    }

    /**
     * 为 Presenter 提供的 View 接口
     */
    interface View extends Contract.View {

        Activity getActivity();

        Context getContext();

        Intent getIntent();
    }

    /**
     * 用于为 IActivity 提供 Intent 构建的接口
     */
    interface Builder<P extends IActivity.Presenter> {

        /**
         * 设置 Presenter
         */
        Builder<P> presenter(Class<? extends P> presenterClass);

        /**
         * 自定义 View 的实现
         * <p>
         * 一般 IActivity 都会有一套默认的实现, 使用该方法将会覆盖原有的实现
         */
        Builder<P> customView(Class<? extends View> viewClass);

        /**
         * 获取 IntentDelegate, 进行 Intent 参数配置
         */
        IntentDelegate intent();

        /**
         * 跳转 IActivity
         */
        void start();
    }
}
