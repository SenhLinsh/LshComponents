package com.linsh.activity;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/19
 *    desc   :
 * </pre>
 */
public interface ITextEditActivity extends IActivity<IActivity.Presenter> {

    interface View extends IActivity.View {
        void setText(String content);
    }

    interface Presenter extends IActivity.Presenter {
        void saveText(String content);
    }
}
