package com.linsh.activity;

import com.linsh.utilseverywhere.interfaces.Function;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/26
 *    desc   : 搜索页面
 * </pre>
 */
public interface ISearchActivity extends IActivity<ISearchActivity.Presenter> {

    interface View extends IActivity.View {

        void setResults(List<? extends CharSequence> results);

        <T> void setResults(List<T> results, Function<CharSequence, T> converter);
    }

    interface Presenter extends IActivity.Presenter<View> {

        void search(String query);

        void onItemClick(int position);

        void onItemLongClick(int position);
    }
}
