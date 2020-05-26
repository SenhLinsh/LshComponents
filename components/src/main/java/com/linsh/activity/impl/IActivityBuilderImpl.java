package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;

import com.linsh.activity.IActivity;
import com.linsh.base.LshActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.activity.IObservableActivity;
import com.linsh.base.activity.IntentDelegate;
import com.linsh.base.mvp.Contract;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
public class IActivityBuilderImpl<P extends IActivity.Presenter> implements IActivity.Builder<P> {

    static final String EXTRA_ACTIVITY_CODE = "activity_code";
    static final String EXTRA_CALLBACK_CODE = "callback_code";
    private final Context context;
    private final IntentDelegate intent;

    public IActivityBuilderImpl(Context context, Class<? extends IActivity.View> iView) {
        this.context = context;
        this.intent = LshActivity.intent(ComponentActivity.class)
                .context(context)
                .putExtra(Contract.View.class.getName(), iView);
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
    public IActivity.Builder<P> callback(IActivity.Callback callback) {
        if (!(context instanceof IObservableActivity)) {
            throw new IllegalArgumentException("context should be Activity");
        }
        CallbackHolder holder = ((IObservableActivity) context).subscribe(CallbackHolder.class);
        int code = callback.hashCode();
        holder.putCallback(code, callback);
        intent.putExtra(EXTRA_CALLBACK_CODE, code);
        intent.putExtra(EXTRA_ACTIVITY_CODE, context.hashCode());
        return this;
    }

    @Override
    public IntentDelegate intent() {
        return intent;
    }

    @Override
    public void start() {
        intent.start();
    }

    public Context getContext() {
        return context;
    }

    public IntentDelegate getIntent() {
        return intent;
    }

    static class CallbackHolder implements ActivitySubscribe {

        private final Map<Integer, IActivity.Callback> callbacks = new HashMap<>();

        private void putCallback(int key, IActivity.Callback callback) {
            callbacks.put(key, callback);
        }

        public IActivity.Callback getCallback(int key) {
            return callbacks.get(key);
        }

        @Override
        public void attach(Activity activity) {
        }
    }
}
