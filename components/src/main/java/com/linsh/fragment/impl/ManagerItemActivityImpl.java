package com.linsh.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linsh.components.R;
import com.linsh.fragment.BaseComponentFragment;
import com.linsh.fragment.IManagerItemFragment;
import com.linsh.lshutils.adapter.SingleItemTypeRcvAdapterEx;
import com.linsh.lshutils.tools.ItemDragHelperEx;
import com.linsh.utilseverywhere.interfaces.Convertible;

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
public class ManagerItemActivityImpl extends BaseComponentFragment<IManagerItemFragment.Presenter> implements IManagerItemFragment {

    private ManagerItemAdapter<Object> adapter;
    private Convertible<Object, CharSequence> textConverter;
    private Convertible<Object, CharSequence> detailConverter;
    private Convertible<Object, Object> iconConverter;
    private boolean isItemViewSwipeEnabled = true;
    private boolean isLongPressDragEnabled = true;

    @Override
    public <T> void setItems(List<? extends CharSequence> items) {
        adapter.setData((List) items);
    }

    @Override
    public <T> void setItems(List<T> items, Convertible<T, CharSequence> textConverter) {
        this.textConverter = (Convertible<Object, CharSequence>) textConverter;
        adapter.setData((List) items);
    }

    @Override
    public <T> void setItems(List<T> items, Convertible<T, CharSequence> textConverter, Convertible<T, CharSequence> detailConverter, Convertible<T, Object> iconConverter) {
        this.textConverter = (Convertible<Object, CharSequence>) textConverter;
        this.detailConverter = (Convertible<Object, CharSequence>) detailConverter;
        this.iconConverter = (Convertible<Object, Object>) iconConverter;
        adapter.setData((List) items);
    }

    @Override
    public void setItemViewSwipeEnabled(boolean enabled) {
        isItemViewSwipeEnabled = enabled;
    }

    @Override
    public void setLongPressDragEnabled(boolean enabled) {
        isLongPressDragEnabled = enabled;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ManagerItemAdapter<Object>();
        adapter.setOnItemClickListener(position -> getPresenter().onItemClick(adapter.getData().get(position), position));
        adapter.setOnItemLongClickListener((view, position) -> getPresenter().onItemLongClick(adapter.getData().get(position), position));
        recyclerView.setAdapter(adapter);

        ItemDragHelperEx itemDragHelper = new ItemDragHelperEx(new ItemDragHelperEx.IItemDragCallback() {
            @Override
            public boolean isItemViewSwipeEnabled() {
                return isItemViewSwipeEnabled;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return isLongPressDragEnabled;
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
        return recyclerView;
    }

    private class ManagerItemAdapter<T> extends SingleItemTypeRcvAdapterEx<T, ManagerItemViewHolder> {

        @Override
        protected int getLayout() {
            return R.layout.components_item_manager;
        }

        @Override
        protected ManagerItemViewHolder createViewHolder(android.view.View view, int viewType) {
            return new ManagerItemViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(ManagerItemViewHolder holder, T item, int position) {
            if (textConverter != null) {
                holder.tvText.setText(textConverter.convert(item));
            } else if (item instanceof CharSequence) {
                holder.tvText.setText((CharSequence) item);
            } else {
                holder.tvText.setText(item.toString());
            }
            if (detailConverter != null) {
                holder.tvDetail.setVisibility(View.VISIBLE);
                holder.tvDetail.setText(detailConverter.convert(item));
            } else {
                holder.tvDetail.setVisibility(View.GONE);
                holder.tvDetail.setText("");
            }
            if (iconConverter != null) {
                holder.ivICon.setVisibility(View.VISIBLE);
                Glide.with(holder.ivICon).load(iconConverter.convert(item)).into(holder.ivICon);
            } else {
                holder.ivICon.setVisibility(View.GONE);
                Glide.with(holder.ivICon).clear(holder.ivICon);
            }
        }
    }

    private static class ManagerItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivICon;
        private final TextView tvText;
        private final TextView tvDetail;

        public ManagerItemViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            ivICon = itemView.findViewById(R.id.iv_components_icon);
            tvText = itemView.findViewById(R.id.tv_components_text);
            tvDetail = itemView.findViewById(R.id.tv_components_detail);
        }
    }
}