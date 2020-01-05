package com.linsh.activity;

import com.linsh.base.activity.Contract;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/04
 *    desc   :
 * </pre>
 */
public interface ManagerItemActivityStarter extends ActivityStarter {

    ManagerItemActivityStarter setTitle(String title);

    ManagerItemActivityStarter setPresenter(Class<? extends Presenter> presenter);

    interface Presenter extends Contract.Presenter<View> {
        boolean isItemViewSwipeEnabled();

        CharSequence getDisplayText(Object item, int position);

        boolean onItemMove(int fromPosition, int toPosition);

        boolean onItemRemove(int position);

        void onItemClick(Object item, int position);

        void onItemLongClick(Object item, int position);

        void saveItems(List<?> items);
    }

    interface View extends Contract.View<Presenter> {
        void setItems(List<?> items);

        void finish();
    }
}
