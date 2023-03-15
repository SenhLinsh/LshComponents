package com.linsh.fragment;

import androidx.fragment.app.Fragment;

import com.linsh.base.mvp.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   : 用于声明 Fragment 页面类型的接口
 * </pre>
 */
public interface IFragment<T extends Contract.Presenter> extends Contract.View {

    Fragment get();

    void setPresenter(T presenter);
}