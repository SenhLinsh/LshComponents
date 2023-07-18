package com.linsh.fragment;

import com.linsh.base.mvp.Contract;
import com.linsh.utilseverywhere.interfaces.Convertible;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/19
 *    desc   : 条目管理 的页面
 *
 *             对集合内的条目进行删除和排序
 * </pre>
 */
public interface IManagerItemFragment extends IFragment<IManagerItemFragment.Presenter> {

    /**
     * 设置数据
     *
     * @param items 数据列表
     */
    <T> void setItems(List<? extends CharSequence> items);

    /**
     * 设置数据
     *
     * @param items         数据列表
     * @param textConverter 内容文本格式转行器，如果为 null，则直接将数据 toString 处理
     */
    <T> void setItems(List<T> items, Convertible<T, CharSequence> textConverter);

    /**
     * 设置数据
     *
     * @param items           数据列表
     * @param textConverter   内容文本格式转行器，如果为 null，则直接将数据 toString 处理
     * @param detailConverter 详细文本格式转行器，如果为 null，则不显示详细文本
     * @param iconConverter   图标格式转行器，如果为 null，则不显示图标
     */
    <T> void setItems(List<T> items, Convertible<T, CharSequence> textConverter, Convertible<T, CharSequence> detailConverter, Convertible<T, Object> iconConverter);

    /**
     * 是否启用滑动删除功能
     * <p>
     * 默认启用
     *
     * @param enabled 是否启用
     */
    void setItemViewSwipeEnabled(boolean enabled);

    /**
     * 是否启用长按拖拽排序功能
     * <p>
     * 默认启用
     *
     * @param enabled 是否启用
     */
    void setLongPressDragEnabled(boolean enabled);

    interface Presenter extends Contract.Presenter {

        boolean onItemMove(int fromPosition, int toPosition);

        /**
         * 触发移除动作
         * <p>
         * 注：View层为了保证数据一致性，会在返回 true 时主动移除数据，Presenter 层请勿再次操作
         *
         * @param position 移除的位置
         * @return true 时确认移除，并将自动删除当前数据。false 为取消移除
         */
        boolean onItemRemove(int position);

        void onItemClick(Object item, int position);

        void onItemLongClick(Object item, int position);
    }
}
