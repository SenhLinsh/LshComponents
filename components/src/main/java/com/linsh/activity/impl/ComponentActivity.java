package com.linsh.activity.impl;

import android.os.Bundle;

import com.linsh.activity.IActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.mvp.Contract;
import com.linsh.base.mvp.BaseMvpActivity;
import com.linsh.base.mvp.Presenter;
import com.linsh.utilseverywhere.ClassUtils;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
@Presenter(ComponentActivity.EmptyPresenter.class)
public class ComponentActivity extends BaseMvpActivity<Contract.Presenter> implements Contract.View {

    private static final String TAG = "ComponentActivity";
    private Contract.View view;
    private IActivity iActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 优先使用 intent 传递过来的 view
        Serializable extra = getIntent().getSerializableExtra(Contract.View.class.getName());
        if (extra != null) {
            try {
                view = (Contract.View) ClassUtils.newInstance((Class) extra, true);
                if (view instanceof ActivitySubscribe) {
                    subscribe((ActivitySubscribe) view);
                }
            } catch (Exception e) {
                throw new RuntimeException("initialize instance of Contract.View failed: " + extra, e);
            }
        }
        // 使用传递过来的 IActivity
        extra = getIntent().getSerializableExtra(IActivity.class.getName());
        if (extra != null) {
            try {
                iActivity = (IActivity) ClassUtils.newInstance((Class) extra, true);
                subscribe((ActivitySubscribe) iActivity);
            } catch (Exception e) {
                throw new RuntimeException("initialize instance of ActivityFuture.IActivity failed: " + extra, e);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Contract.View initContractView() {
        if (view != null)
            return view;
        return this;
    }

    @Override
    public Contract.Presenter getPresenter() {
        return super.getPresenter();
    }

    IActivity getIActivity() {
        return iActivity;
    }

    static class EmptyPresenter implements Contract.Presenter {
        @Override
        public void attachView(Contract.View view) {
        }

        @Override
        public void detachView() {
        }
    }
}
