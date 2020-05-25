package com.linsh.view.tag;

import com.linsh.view.IView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/25
 *    desc   : 标签控件
 * </pre>
 */
public interface ITagView extends IView {

    void setText(CharSequence text);

    CharSequence getText();
}
