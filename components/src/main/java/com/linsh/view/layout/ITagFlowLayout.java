package com.linsh.view.layout;

import android.view.View;

import com.linsh.utilseverywhere.interfaces.Consumer;
import com.linsh.utilseverywhere.interfaces.Consumer2;
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

    ITagFlowLayout setTags(List<? extends CharSequence> tags);

    <T> ITagFlowLayout setTags(List<T> tags, Convertible<T, CharSequence> convertible);

    ITagFlowLayout setOnTagClickListener(OnItemClickListener listener);

    ITagFlowLayout setOnTagLongClickListener(OnItemLongClickListener listener);

    /**
     * 适配 generateView 操作
     *
     * @param consumer 需要调整的 TagView
     */
    ITagFlowLayout adaptGenerateView(Consumer<View> consumer);

    /**
     * 适配 bingView 操作
     *
     * @param consumer 参数1 为 TagView, 参数2 为 position
     */
    ITagFlowLayout adaptBindView(Consumer2<View, Integer> consumer);
}
