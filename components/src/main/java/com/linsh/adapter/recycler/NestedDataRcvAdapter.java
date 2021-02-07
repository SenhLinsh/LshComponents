package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.lshutils.entity.NestedInfo;
import com.linsh.lshutils.tools.NestedList;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.IView;
import com.linsh.view.ViewComponents;
import com.linsh.views.R;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   : 为多层嵌套数据提供的可展开 Item 的 Adapter
 *
 *             数据层必须实现 NestedInfo 接口
 * </pre>
 */
public abstract class NestedDataRcvAdapter<T extends NestedInfo, P extends IView, C extends IView>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int ITEM_VIEW_TYPE_PARENT = 1;
    private static final int ITEM_VIEW_TYPE_CHILD = 2;

    private NestedList nestedList;

    public void setData(List<T> data) {
        if (nestedList == null) {
            nestedList = new NestedList(data);
        } else {
            nestedList.setNestedInfos(data);
        }
        notifyDataSetChanged();
    }

    public void setData(NestedList nestedList) {
        this.nestedList = nestedList;
        notifyDataSetChanged();
    }

    public NestedInfo getNestedInfo(int position) {
        return nestedList.get(position);
    }

    @Override
    public int getItemCount() {
        return nestedList != null ? nestedList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        NestedInfo nestedInfo = getNestedInfo(position);
        if (nestedInfo != null) {
            return nestedInfo.isNested() ? ITEM_VIEW_TYPE_PARENT : ITEM_VIEW_TYPE_CHILD;
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

        NestedInfo nestedInfo = getNestedInfo(position);

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            onBindViewHolder(((ParentViewHolder) holder).viewHelper, nestedInfo);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            onBindViewHolder(((ChildViewHolder) holder).viewHelper, nestedInfo);
        }
    }

    public abstract void onBindViewHolder(IView view, NestedInfo item);

    @Override
    public void onClick(View v) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        int position = holder.getAdapterPosition();
        NestedInfo nestedInfo = getNestedInfo(position);

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            nestedList.toggle(position);
            notifyDataSetChanged();
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(((ParentViewHolder) holder).viewHelper, nestedInfo);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(((ChildViewHolder) holder).viewHelper, nestedInfo);
        }
    }

    private NestedDataRcvAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(NestedDataRcvAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(IView viewHelper, NestedInfo item);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener == null)
            return false;

        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        int position = holder.getAdapterPosition();
        NestedInfo nestedInfo = getNestedInfo(position);

        if (holder instanceof NestedDataRcvAdapter.ParentViewHolder) {
            mOnItemLongClickListener.onItemLongClick(((ParentViewHolder) holder).viewHelper, nestedInfo);
        } else if (holder instanceof NestedDataRcvAdapter.ChildViewHolder) {
            mOnItemLongClickListener.onItemLongClick(((ChildViewHolder) holder).viewHelper, nestedInfo);
        }
        return true;
    }


    private NestedDataRcvAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(NestedDataRcvAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(IView viewHelper, NestedInfo item);
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
}
