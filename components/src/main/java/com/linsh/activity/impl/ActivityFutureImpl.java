package com.linsh.activity.impl;

import android.content.Context;

import com.linsh.activity.ActivityFuture;
import com.linsh.activity.IActivity;
import com.linsh.base.LshActivity;
import com.linsh.base.mvp.Contract;
import com.linsh.base.activity.IntentDelegate;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
abstract class ActivityFutureImpl implements ActivityFuture {

    private final Context context;
    private final IntentDelegate intent;

    public ActivityFutureImpl(Context context) {
        this.context = context;
        this.intent = LshActivity.intent(ComponentActivity.class);
    }

    @Override
    public ActivityFuture subscribeActivity(Class<? extends IActivity> clazz) {
        intent.putExtra(IActivity.class.getName(), clazz);
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

    protected void setViewInstance(Class<? extends Contract.View> view) {
        intent.putExtra(Contract.View.class.getName(), view);
    }
}
