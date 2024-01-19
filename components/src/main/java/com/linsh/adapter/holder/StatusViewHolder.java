package com.linsh.adapter.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linsh.base.adapter.DataAdaptedRcvAdapter;
import com.linsh.components.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2024/01/19
 *    desc   : 用于 RecyclerView 的状态 ViewHolder
 * </pre>
 */
public class StatusViewHolder extends DataAdaptedRcvAdapter.DataAdaptedViewHolder<StatusViewHolder.EmptyStatus> {
    public ImageView ivImage;
    public TextView tvText;

    public StatusViewHolder(ViewGroup parent) {
        super(parent, R.layout.components_item_status);
        ivImage = itemView.findViewById(R.id.iv_cpn_item_status);
        tvText = itemView.findViewById(R.id.tv_cpn_item_status);
    }

    @Override
    public void onBindItem(@NonNull EmptyStatus item, int position) {
        switch (item) {
            case EMPTY:
                ivImage.setImageResource(R.drawable.components_ic_status_empty);
                tvText.setText("这里空空如也...");
                break;
            case ERROR:
                ivImage.setImageResource(R.drawable.components_ic_status_failed2);
                tvText.setText("数据好像迷路了...");
                break;
            case OVERLOAD:
                ivImage.setImageResource(R.drawable.components_ic_status_failed);
                tvText.setText("CPU被干烧了呢...");
                break;
        }
    }

    public enum EmptyStatus {
        EMPTY, ERROR, OVERLOAD
    }
}
