package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.IView;
import com.linsh.view.ViewComponents;
import com.linsh.components.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   : 为多层嵌套数据提供的可展开 Item 的 Adapter
 *
 *             注: 该 Adapter 在同一个层级最多只展开一个 Item
 * </pre>
 *
 * @param <T> 数据类型声明
 * @param <P> 可展开的父 Item 的类型声明
 * @param <C> 不可展开的子 Item 的类型声明
 */
public abstract class NestedDataRcvAdapter2<T, P extends IView, C extends IView>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int ITEM_VIEW_TYPE_PARENT = 1;
    private static final int ITEM_VIEW_TYPE_CHILD = 2;

    /*根数据*/
    private List<T> data;
    /*
     * 当前列表展示的数据
     * 1. index 0 为根数据, 往后为每一层打开的数据
     * 2. 往后每次层, 都为前一层的 sublist, 因此, index 0 的数据即为包含 sublist 的所有数据
     */
    private List<SubList<T>> items = new ArrayList<>();

    public void setData(List<T> data) {
        this.data = data;
        // 更新展开的数据
        if (items.size() == 0) {
            items.add(new SubList<T>(new ArrayList<T>(data)));
        } else {
            items.set(0, new SubList<T>(new ArrayList<T>(data)));
        }
        if (items.size() > 1) {
            for (int i = 1; i < items.size(); i++) {
                SubList<T> subList = items.get(i);
                SubList<T> parent = items.get(i - 1);
                int offset = subList.getOffset();
                // 判断原有的 offset 在新的 parent 中是否有效
                if (offset < parent.size() - 1 && isParent(parent.get(offset - 1))) {
                    // 如果有效, 则继续展开
                    SubList<T> newSubList = parent.subList(offset, offset);
                    newSubList.addAll(getChildren(parent.get(offset - 1)));
                    items.set(i, newSubList);
                } else {
                    removeSublist(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public abstract boolean isParent(T item);

    public abstract List<T> getChildren(T item);

    public List<T> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        if (items.size() == 0)
            return 0;
        return items.get(0).size();
    }

    @Override
    public int getItemViewType(int position) {
        T item = items.get(0).get(position);
        return isParent(item) ? ITEM_VIEW_TYPE_PARENT : ITEM_VIEW_TYPE_CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ITEM_VIEW_TYPE_PARENT) {
            Class<P> parentClass = (Class<P>) ClassUtils.getGenericType(getClass(), NestedDataRcvAdapter2.class, 1);
            P parentViewHelper = ViewComponents.create(parent.getContext(), parentClass);
            holder = new ParentViewHolder(parentViewHelper.getView(), parentViewHelper);
            parentViewHelper.getView().setTag(R.id.tag_view_helper, parentViewHelper);
        } else if (viewType == ITEM_VIEW_TYPE_CHILD) {
            Class<C> childClass = (Class<C>) ClassUtils.getGenericType(getClass(), NestedDataRcvAdapter2.class, 2);
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

        T item = items.get(0).get(position);

        if (holder instanceof NestedDataRcvAdapter2.ParentViewHolder) {
            onBindParentViewHelper(((ParentViewHolder) holder).viewHelper, item, isToggle(0, position));
        } else if (holder instanceof NestedDataRcvAdapter2.ChildViewHolder) {
            onBindChildViewHelper(((ChildViewHolder) holder).viewHelper, item);
        }
    }

    public abstract void onBindParentViewHelper(P parent, T item, boolean isOpened);

    public abstract void onBindChildViewHelper(C child, T item);

    @Override
    public void onClick(View v) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        int position = holder.getAdapterPosition();
        T item = items.get(0).get(position);

        if (holder instanceof NestedDataRcvAdapter2.ParentViewHolder) {
            boolean toggle = toggle(0, position);
            notifyDataSetChanged();
            if (mOnItemClickListener != null)
                mOnItemClickListener.onParentClick(((ParentViewHolder) holder).viewHelper, item, toggle);
        } else if (holder instanceof NestedDataRcvAdapter2.ChildViewHolder) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onChildClick(((ChildViewHolder) holder).viewHelper, item);
        }
    }

    private boolean isToggle(int subIndex, int position) {
        if (subIndex + 1 >= items.size()) {
            // 没有子 sublist, 说明没有 item 展开
            return false;
        }
        SubList<T> child = items.get(subIndex + 1);
        int offset = child.getOffset();
        if (position == offset - 1) {
            // 位于 sublist 的父 Item 位置, 已展开
            return true;
        }
        if (position >= offset && position < offset + child.size()) {
            // 位于 sublist 内, 递归
            return isToggle(subIndex + 1, position - offset);
        }
        // 位于 sublist 之外
        return false;
    }

    private boolean toggle(int subIndex, int position) {
        SubList<T> current = items.get(subIndex);
        if (subIndex + 1 >= items.size()) {
            // 没有子 sublist, 说明没有 item 展开, 直接展开
            SubList<T> child = current.subList(position + 1, position + 1);
            child.addAll(getChildren(current.get(position)));
            items.add(child);
            return true;
        }
        SubList<T> child = items.get(subIndex + 1);
        int offset = child.getOffset();
        if (position == offset - 1) {
            // 位于 sublist 的父 Item 位置, 已展开, 进行关闭
            child.clear();
            removeSublist(subIndex + 1);
            return false;
        }
        if (position >= offset && position < offset + child.size()) {
            // 位于 sublist 内, 递归
            return toggle(subIndex + 1, position - offset);
        }
        // 位于 sublist 之外, 关闭展开的 item, 再展开当前 item
        position = position - child.size();
        child.clear();
        removeSublist(subIndex + 1);

        child = current.subList(position + 1, position + 1);
        child.addAll(getChildren(current.get(position)));
        items.add(child);
        return true;
    }

    private void removeSublist(int index) {
        while (index < items.size()) {
            items.remove(index);
        }
    }

    private NestedDataRcvAdapter2.OnItemClickListener<T, P, C> mOnItemClickListener;

    public void setOnItemClickListener(NestedDataRcvAdapter2.OnItemClickListener<T, P, C> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T, P extends IView, C extends IView> {
        void onParentClick(P parentView, T item, boolean isOpened);

        void onChildClick(C childView, T item);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener == null)
            return false;

        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.tag_view_holder);
        int position = holder.getAdapterPosition();
        T item = items.get(0).get(position);

        if (items.size() == 1) {
            // 没有已展开的 item, 直接展开
            SubList<T> sublist = items.get(0).subList(position + 1, position + 1);
            sublist.addAll(getChildren(item));
            items.add(sublist);
            return true;
        }
        boolean toggle = toggle(1, position);

        if (holder instanceof NestedDataRcvAdapter2.ParentViewHolder) {
            mOnItemLongClickListener.onParentLongClick(((ParentViewHolder) holder).viewHelper, item, toggle);
        } else if (holder instanceof NestedDataRcvAdapter2.ChildViewHolder) {
            mOnItemLongClickListener.onChildLongClick(((ChildViewHolder) holder).viewHelper, item);
        }
        return true;
    }

    private NestedDataRcvAdapter2.OnItemLongClickListener<T, P, C> mOnItemLongClickListener;

    public void setOnItemClickListener(NestedDataRcvAdapter2.OnItemLongClickListener<T, P, C> listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener<T, P extends IView, C extends IView> {
        void onParentLongClick(P parentView, T item, boolean isOpened);

        void onChildLongClick(C childView, T item);
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

    private static class SubList<E> extends ArrayList<E> {

        private final List<E> parent;
        private final int offset;
        private int size;

        SubList(List<E> list) {
            this(list, 0, list.size());
        }

        SubList(List<E> list, int fromIndex, int toIndex) {
            this.parent = list;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        int getOffset() {
            return offset;
        }

        List<E> getParent() {
            return parent;
        }

        @Override
        public E set(int index, E element) {
            return parent.set(index + offset, element);
        }

        @Override
        public E get(int index) {
            return parent.get(offset + index);
        }

        @Override
        public E remove(int index) {
            size--;
            return parent.remove(offset + index);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return addAll(size, c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            int cSize = c.size();
            if (cSize == 0)
                return false;
            parent.addAll(offset + index, c);
            size += cSize;
            return true;
        }

        @Override
        public void clear() {
            for (int i = 0; i < size; i++)
                parent.remove(offset);
            size = 0;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public SubList<E> subList(int fromIndex, int toIndex) {
            return new SubList<>(this, fromIndex, toIndex);
        }
    }
}
