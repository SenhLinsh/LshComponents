package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.activity.IActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.mvp.BaseMvpActivity;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/20
 *    desc   :
 * </pre>
 */
public class IActivityViewImpl<P extends IActivity.Presenter> implements IActivity.View, ActivitySubscribe {

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

    protected P getPresenter() {
        return (P) ((ComponentActivity) activity).getPresenter();
    }
}
