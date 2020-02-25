package com.linsh.view.layout;

import android.view.View;

import com.linsh.utilseverywhere.interfaces.Consumer;
import com.linsh.utilseverywhere.interfaces.Convertible;
import com.linsh.view.IView;
import com.linsh.view.OnItemClickListener;
import com.linsh.view.OnItemLongClickListener;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/23
 *    desc   : 流式标签布局
 * </pre>
 */
public interface ITagFlowLayout extends IView {

    void setTags(List<? extends CharSequence> tags);

    <T> void setTags(List<T> tags, Convertible<T, CharSequence> convertible);

    void setOnTagClickListener(OnItemClickListener listener);

    void setOnTagLongClickListener(OnItemLongClickListener listener);

    /**
     * 适配 generateView 操作
     *
     * @param consumer 需要调整的 TagView
     */
    void adaptGenerateView(Consumer<View> consumer);

    /**
     * 适配 bingView 操作
     */
    void adaptBindView(Consumer<View> consumer);
}
