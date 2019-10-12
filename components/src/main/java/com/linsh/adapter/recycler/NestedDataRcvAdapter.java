package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import com.linsh.bean.NestedInfo;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.ViewComponents;
import com.linsh.view.ViewHelper;
import com.linsh.views.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   :
 * </pre>
 */
public abstract class NestedDataRcvAdapter<T extends NestedInfo, P extends ViewHelper, C extends ViewHelper>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int ITEM_VIEW_TYPE_PARENT = 1;
    private static final int ITEM_VIEW_TYPE_CHILD = 2;

    private List<T> data;
    private List<NestedItem> items = new ArrayList<>();

    public void setData(List<T> data) {
        this.data = data;
        items.clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                items.add(new NestedItem(new int[]{i}));
            }
        }
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        NestedItem item = items.get(position);
        NestedInfo t = null;
        for (int i = 0; i < item.position.length; i++) {
            if (i == 0) {
                t = data.get(item.position[i]);
            } else {
                t = t.getChildren().get(item.position[i]);
            }
        }
        if (t != null) {
            return t.isNested() ? ITEM_VIEW_TYPE_PARENT : ITEM_VIEW_TYPE_CHILD;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ITEM_VIEW_TYPE_PARENT) {
            Class<P> parentClass = (Class<P>) ClassUtils.getGenericType(getClass(), NestedDataRcvAdapter.class, 1);
            P parentViewHelper = ViewComponents.create(parent.getContext(), parentClass);
            holder = new ParentViewHolder(parentViewHelper.getView(), parentViewHelper);
            parentViewHelper.getView().setTag(R.id.tag_view_helper, parentViewHelper);
        } else if (viewType == ITEM_VIEW_TYPE_CHILD) {
            Class<C> childClass = (Class<C>) ClassUtils.getGenericType(getClass(), NestedDataRcvAdapter.class, 2);
            C childViewHelper = ViewComponents.create(parent.getContext(), childClass);
            holder = new ChildViewHolder(childViewHelper.getView(), childViewHelper);
            childViewHelper.getView().setTag(R.id.tag_view_helper, childViewHelper);
        } else {
            throw new IllegalStateException("unknown item type");
        }
        View view = holder.itemView;
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(R.id.tag_view_holder, holder);

        NestedItem item = items.get(position);
        NestedInfo t = null;
        for (int i = 0; i < item.position.length; i++) {
            if (i == 0) {
                t = data.get(item.position[i]);
            } else {
                t = t.getChildren().get(item.position[i]);
            }
        }

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            onBindViewHelper(((ParentViewHolder) holder).viewHelper, null, t, item.position);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            onBindViewHelper(null, ((ChildViewHolder) holder).viewHelper, t, item.position);
        }
    }

    public abstract void onBindViewHelper(P parent, C child, NestedInfo data, int[] position);

    @Override
    public void onClick(View v) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        int position = holder.getAdapterPosition();
        NestedItem item = items.get(position);
        NestedInfo t = null;
        for (int i = 0; i < item.position.length; i++) {
            if (i == 0) {
                t = data.get(item.position[i]);
            } else {
                t = t.getChildren().get(item.position[i]);
            }
        }

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            t.setOpened(!t.isOpened());
            if (t.isOpened()) {
                List<? extends NestedInfo> children = t.getChildren();
                for (int i = 0; i < children.size(); i++) {
                    int[] positions = items.get(position).position;
                    positions = Arrays.copyOf(positions, positions.length + 1);
                    positions[positions.length - 1] = i;
                    items.add(position + 1, new NestedItem(positions));
                }
            } else {
                int[] positions = items.get(position).position;
                int next = position + 1;
                while (next < items.size() && items.get(next).position.length > positions.length) {
                    items.remove(next);
                }
            }
            notifyDataSetChanged();
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(((ParentViewHolder) holder).viewHelper, t, item.position);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(((ChildViewHolder) holder).viewHelper, t, item.position);
        }
    }

    private NestedDataRcvAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(NestedDataRcvAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHelper viewHelper, NestedInfo data, int[] position);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener == null)
            return false;

        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        NestedItem item = items.get(holder.getAdapterPosition());
        NestedInfo t = null;
        for (int i = 0; i < item.position.length; i++) {
            if (i == 0) {
                t = data.get(item.position[i]);
            } else {
                t = t.getChildren().get(item.position[i]);
            }
        }

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            mOnItemLongClickListener.onItemLongClick(((ParentViewHolder) holder).viewHelper, t, item.position);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            mOnItemLongClickListener.onItemLongClick(((ChildViewHolder) holder).viewHelper, t, item.position);
        }
        return true;
    }


    private NestedDataRcvAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(NestedDataRcvAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ViewHelper viewHelper, NestedInfo data, int[] position);
    }

    private class ParentViewHolder extends RecyclerView.ViewHolder {

        private final P viewHelper;

        public ParentViewHolder(@NonNull View itemView, P viewHelper) {
            super(itemView);
            this.viewHelper = viewHelper;
        }
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {

        private final C viewHelper;

        public ChildViewHolder(@NonNull View itemView, C viewHelper) {
            super(itemView);
            this.viewHelper = viewHelper;
        }
    }

    private static class NestedItem {

        private final int[] position;

        private NestedItem(int[] position) {
            this.position = position;
        }
    }
}
