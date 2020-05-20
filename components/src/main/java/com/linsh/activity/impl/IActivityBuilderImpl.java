package com.linsh.activity.impl;

import android.content.Context;

import com.linsh.activity.IActivity;
import com.linsh.base.LshActivity;
import com.linsh.base.activity.IntentDelegate;
import com.linsh.base.mvp.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
public class IActivityBuilderImpl<P extends IActivity.Presenter> implements IActivity.Builder<P> {

    private final Context context;
    private final IntentDelegate intent;

    public IActivityBuilderImpl(Context context, Class<? extends IActivity.View> iView) {
        this.context = context;
        this.intent = LshActivity.intent(ComponentActivity.class);
        intent.putExtra(Contract.View.class.getName(), iView);
    }

    @Override
    public IActivity.Builder<P> presenter(Class<? extends P> clazz) {
        intent.presenter(clazz);
        return this;
    }

    @Override
    public IActivity.Builder<P> customView(Class<? extends IActivity.View> viewClass) {
        intent.putExtra(Contract.View.class.getName(), viewClass);
        return this;
    }

    @Override
    public IntentDelegate intent() {
        return intent;
    }

    @Override
    public void start() {
        intent.start(context);
    }

    public Context getContext() {
        return context;
    }

    public IntentDelegate getIntent() {
        return intent;
    }
}
