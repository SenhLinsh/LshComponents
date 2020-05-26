package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.activity.IManagerItemActivity;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.base.mvp.Contract;
import com.linsh.lshutils.adapter.SingleItemTypeRcvAdapterEx;
import com.linsh.lshutils.tools.ItemDragHelperEx;
import com.linsh.views.R;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/19
 *    desc   :
 * </pre>
 */
class ManagerItemActivityImpl extends IActivityViewImpl<IManagerItemActivity.Presenter> implements IManagerItemActivity.View, ActivitySubscribe.OnCreate,
        ActivitySubscribe.OnCreateOptionsMenu, ActivitySubscribe.OnOptionsItemSelected {

    private ComponentActivity activity;
    private ManagerItemAdapter adapter;

    @Override
    public void attach(Activity activity) {
        this.activity = (ComponentActivity) activity;
    }

    @Override
    public void setItems(List<?> items) {
        adapter.setData(items);
    }

    @Override
    public void finish() {
        activity.finish();
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Intent getIntent() {
        return activity.getIntent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String title = activity.getIntent().getStringExtra("title");
        if (title != null) {
            activity.setTitle(title);
        }

        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        activity.setContentView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ManagerItemAdapter() {
            @Override
            protected CharSequence getDisplayText(Object item, int position) {
                return getPresenter().getDisplayText(item, position);
            }
        };
        adapter.setOnItemClickListener(new SingleItemTypeRcvAdapterEx.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getPresenter().onItemClick(adapter.getData().get(position), position);
            }
        });
        adapter.setOnItemLongClickListener(new SingleItemTypeRcvAdapterEx.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(android.view.View view, int position) {
                getPresenter().onItemLongClick(adapter.getData().get(position), position);
            }
        });
        recyclerView.setAdapter(adapter);

        ItemDragHelperEx itemDragHelper = new ItemDragHelperEx(new ItemDragHelperEx.IItemDragCallback() {
            @Override
            public boolean isItemViewSwipeEnabled() {
                return getPresenter().isItemViewSwipeEnabled();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, int fromPosition, int toPosition) {
                if (getPresenter().onItemMove(fromPosition, toPosition)) {
                    Collections.swap(adapter.getData(), fromPosition, toPosition);
                    adapter.notifyItemMoved(fromPosition, toPosition);
                    return true;
                }
                return false;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, int fromPosition, int toPosition) {
            }

            @Override
            public void onSwiped(int position, int direction) {
                if (getPresenter().onItemRemove(position)) {
                    adapter.getData().remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    adapter.notifyItemChanged(position);
                }
            }
        });
        itemDragHelper.attachToRecyclerView(recyclerView);
    }

    public IManagerItemActivity.Presenter getPresenter() {
        Contract.Presenter presenter = activity.getPresenter();
        if (presenter instanceof IManagerItemActivity.Presenter) {
            return (IManagerItemActivity.Presenter) presenter;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private abstract static class ManagerItemAdapter<T> extends SingleItemTypeRcvAdapterEx<T, ManagerItemViewHolder> {

        @Override
        protected int getLayout() {
            return R.layout.item_type_edit;
        }

        @Override
        protected ManagerItemViewHolder createViewHolder(android.view.View view, int viewType) {
            return new ManagerItemViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(ManagerItemViewHolder holder, Object item, int position) {
            holder.tvText.setText(getDisplayText(item, position));
        }

        protected abstract CharSequence getDisplayText(Object item, int position);
    }

    private static class ManagerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvText;

        public ManagerItemViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_item_type_edit_type_name);
        }
    }
}