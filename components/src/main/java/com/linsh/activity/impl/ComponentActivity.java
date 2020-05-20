package com.linsh.activity.impl;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.mvp.BaseMvpActivity;
import com.linsh.base.mvp.Contract;
import com.linsh.base.mvp.Presenter;
import com.linsh.utilseverywhere.ClassUtils;

import java.io.Serializable;

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

    static class EmptyPresenter implements Contract.Presenter {
        @Override
        public void attachView(Contract.View view) {
        }

        @Override
        public void detachView() {
        }
    }
}
