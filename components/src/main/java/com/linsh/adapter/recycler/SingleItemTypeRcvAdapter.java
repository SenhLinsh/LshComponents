package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.view.ViewComponents;
import com.linsh.view.ViewHelper;
import com.linsh.views.R;

import java.lang.reflect.Type;
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
public abstract class SingleItemTypeRcvAdapter<T, V extends ViewHelper>
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

    public abstract void onBindViewHelper(V viewHelper, T data, int position);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    static class ViewHolder<V extends ViewHelper> extends RecyclerView.ViewHolder {

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
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(viewHelper, holder.getAdapterPosition());
        }
    }

    private SingleItemTypeRcvAdapter.OnItemClickListener<V> mOnItemClickListener;

    public void setOnItemClickListener(SingleItemTypeRcvAdapter.OnItemClickListener<V> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<V extends ViewHelper> {
        void onItemClick(V viewHelper, int position);
    }

    @Override
    public boolean onLongClick(View v) {
        Object tag = v.getTag(R.id.tag_view_holder);
        if (tag instanceof SingleItemTypeRcvAdapter.ViewHolder) {
            SingleItemTypeRcvAdapter.ViewHolder<V> holder = (SingleItemTypeRcvAdapter.ViewHolder) tag;
            V viewHelper = holder.viewHelper;
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemLongClick(viewHelper, holder.getAdapterPosition());
                return true;
            }
        }
        return false;
    }

    private SingleItemTypeRcvAdapter.OnItemLongClickListener<V> mOnItemLongClickListener;

    public void setOnItemLongClickListener(SingleItemTypeRcvAdapter.OnItemLongClickListener<V> listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener<V extends ViewHelper> {
        void onItemLongClick(V viewHelper, int position);
    }
}
