package com.linsh.fragment;

import com.linsh.base.mvp.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/06
 *    desc   : 简单工具页面
 *
 *             包含自定义添加的按钮，及 Log 信息展示部分
 * </pre>
 */
public interface ISimpleToolFragment extends IFragment<ISimpleToolFragment.Presenter> {

    void buildButton(String id, String name);

    void setContent(String content);

    void addLog(String log);

    void clearLogs();

    interface Presenter extends Contract.Presenter {

        void onButtonClick(String id);
    }
}