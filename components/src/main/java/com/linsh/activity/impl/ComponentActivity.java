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
 *    desc   : ActivityComponents 默认跳转的通用型 Activity
 *
 *             该 Activity 默认无任何内容, 页面内容通过注册时 {@link com.linsh.activity.ActivityComponents#register(Class, Class)}
 *             所设置的 IView 进行实现.
 *
 *             IView 实际上是 Contract.View 的实现, 进行页面构建. 而 Contract.Presenter 则是通过 IActivity.Builder
 *             进行指定, 通过 intent 传递到当前页面
 * </pre>
 */
// 使用 EmptyPresenter 进行保底, 如果 intent 传递了 Presenter, 则优先用后者
@Presenter(ComponentActivity.EmptyPresenter.class)
public class ComponentActivity extends BaseMvpActivity<Contract.Presenter> implements Contract.View {

    private Contract.View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 优先使用 intent 传递过来的 view
        Serializable extra = getIntent().getSerializableExtra(Contract.View.class.getName());
        if (extra != null) {
            try {
                view = (Contract.View) ClassUtils.newInstance((Class<?>) extra, true);
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
