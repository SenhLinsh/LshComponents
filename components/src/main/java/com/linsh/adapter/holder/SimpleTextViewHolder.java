package com.linsh.adapter.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linsh.base.adapter.DataAdaptedRcvAdapter;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2024/01/19
 *    desc   : 简单文本 ViewHolder
 * </pre>
 */
public class SimpleTextViewHolder extends DataAdaptedRcvAdapter.DataAdaptedViewHolder<String> {

    public SimpleTextViewHolder(@NonNull ViewGroup parent) {
        super(parent, new TextView(parent.getContext()));
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onBindItem(@NonNull String item, int position) {
        ((TextView) itemView).setText(item);
    }
}
