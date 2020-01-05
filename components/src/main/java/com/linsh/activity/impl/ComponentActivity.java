package com.linsh.activity.impl;

import android.os.Bundle;

import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.activity.Contract;
import com.linsh.base.activity.mvp.BaseMvpActivity;
import com.linsh.base.activity.mvp.Presenter;
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
public class ComponentActivity extends BaseMvpActivity<Contract.Presenter> implements Contract.View<Contract.Presenter> {

    private static final String TAG = "ComponentActivity";
    private Contract.View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Contract.View initContractView() {
        if (view != null)
            return view;
        return this;
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
