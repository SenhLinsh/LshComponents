package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.activity.IActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.activity.IObservableActivity;
import com.linsh.base.mvp.BaseMvpViewImpl;
import com.linsh.lshutils.utils.ActivityLifecycleUtilsEx;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/20
 *    desc   :
 * </pre>
 */
public class IActivityViewImpl<P extends IActivity.Presenter> extends BaseMvpViewImpl<P> implements IActivity.View, ActivitySubscribe {

    private Activity activity;

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Intent getIntent() {
        return activity.getIntent();
    }

    @Override
    public void onCallback(Object data) {
        int activityCode = activity.getIntent().getIntExtra(IActivityBuilderImpl.EXTRA_ACTIVITY_CODE, 0);
        int callbackCode = activity.getIntent().getIntExtra(IActivityBuilderImpl.EXTRA_CALLBACK_CODE, 0);
        if (activityCode != 0 && callbackCode != 0) {
            Activity callbackActivity = ActivityLifecycleUtilsEx.findCreatedActivity(activityCode);
            if (callbackActivity instanceof IObservableActivity) {
                IActivityBuilderImpl.CallbackHolder holder = ((IObservableActivity) callbackActivity).subscribe(IActivityBuilderImpl.CallbackHolder.class);
                IActivity.Callback callback = holder.getCallback(callbackCode);
                if (callback != null) {
                    callback.onCallback(data);
                }
            }
        }
    }
}
