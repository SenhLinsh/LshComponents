package com.linsh.fragment;

import androidx.fragment.app.Fragment;

import com.linsh.base.mvp.BaseMvpFragment;
import com.linsh.base.mvp.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   :
 * </pre>
 */
public class BaseComponentFragment<P extends Contract.Presenter> extends BaseMvpFragment<P> implements IFragment<P> {

    private P presenter;

    @Override
    public Fragment get() {
        return this;
    }

    @Override
    protected P initContractPresenter() {
        if (presenter == null)
            throw new RuntimeException("请先设置 Presenter");
        return presenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
