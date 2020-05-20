package com.linsh.activity;

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
public interface IManagerItemActivity extends IActivity {

    interface View extends IActivity.View {

        void setItems(List<?> items);

        void finish();
    }

    interface Presenter extends IActivity.Presenter<View> {

        boolean isItemViewSwipeEnabled();

        CharSequence getDisplayText(Object item, int position);

        boolean onItemMove(int fromPosition, int toPosition);

        boolean onItemRemove(int position);

        void onItemClick(Object item, int position);

        void onItemLongClick(Object item, int position);

        void saveItems(List<?> items);
    }
}
