package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linsh.components.R;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.IView;
import com.linsh.view.ViewComponents;

import java.lang.reflect.Type;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/11
 *    desc   : 简单抽取的 RecyclerView Adapter 基类, 适用于单条目类型
 * </pre>
 */
public abstract class SingleItemTypeRcvAdapter<T, V extends IView>
        extends RecyclerView.Adapter<SingleItemTypeRcvAdapter.ViewHolder<V>>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "SingleItemTypeRcvAdapte";
    private List<T> data;

    @NonNull
    @Override
    public ViewHolder<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Type type = ClassUtils.getGenericType(getClass(), SingleItemTypeRcvAdapter.class, 1);
        V viewHelper = ViewComponents.create(parent.getContext(), (Class<V>) type);
        View view = viewHelper.getView();
        view.setTag(R.id.tag_view_helper, viewHelper);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder<>(view, viewHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemTypeRcvAdapter.ViewHolder<V> holder, int position) {
        holder.itemView.setTag(R.id.tag_view_holder, holder);
        V viewHelper = holder.viewHelper;
        onBindViewHelper(viewHelper, data == null ? null : data.get(position), position);
    }

    public abstract void onBindViewHelper(V viewHelper, T item, int position);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    static class ViewHolder<V extends IView> extends RecyclerView.ViewHolder {

        private V viewHelper;

        public ViewHolder(@NonNull View itemView, V viewHelper) {
            super(itemView);
            this.viewHelper = viewHelper;
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag(R.id.tag_view_holder);
        if (tag instanceof SingleItemTypeRcvAdapter.ViewHolder) {
            SingleItemTypeRcvAdapter.ViewHolder<V> holder = (SingleItemTypeRcvAdapter.ViewHolder) tag;
            V viewHelper = holder.viewHelper;
            if (mOnItemClickListener != null) {
                int position = holder.getAdapterPosition();
                mOnItemClickListener.onItemClick(viewHelper, data.get(position), position);
            }
        }
    }

    private SingleItemTypeRcvAdapter.OnItemClickListener<T, V> mOnItemClickListener;

    public void setOnItemClickListener(SingleItemTypeRcvAdapter.OnItemClickListener<T, V> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T, V extends IView> {
        void onItemClick(V viewHelper, T item, int position);
    }

    @Override
    public boolean onLongClick(View v) {
        Object tag = v.getTag(R.id.tag_view_holder);
        if (tag instanceof SingleItemTypeRcvAdapter.ViewHolder) {
            SingleItemTypeRcvAdapter.ViewHolder<V> holder = (SingleItemTypeRcvAdapter.ViewHolder) tag;
            V viewHelper = holder.viewHelper;
            if (mOnItemLongClickListener != null) {
                int position = holder.getAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(viewHelper, data.get(position), position);
                return true;
            }
        }
        return false;
    }

    private SingleItemTypeRcvAdapter.OnItemLongClickListener<T, V> mOnItemLongClickListener;

    public void setOnItemLongClickListener(SingleItemTypeRcvAdapter.OnItemLongClickListener<T, V> listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener<T, V extends IView> {
        void onItemLongClick(V viewHelper, T item, int position);
    }
}
