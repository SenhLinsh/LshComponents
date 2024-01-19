package com.linsh.adapter.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linsh.base.adapter.DataAdaptedRcvAdapter;
import com.linsh.lshutils.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2024/01/19
 *    desc   : 底部 Footer ViewHolder
 * </pre>
 */
public class BottomFooterViewHolder extends DataAdaptedRcvAdapter.DataAdaptedViewHolder<BottomFooterViewHolder.Data> {
    public TextView tvBottom;

    public BottomFooterViewHolder(ViewGroup parent) {
        super(parent, R.layout.uee_item_footer_bottom);
        tvBottom = (TextView) itemView;
    }

    @Override
    public void onBindItem(@NonNull Data item, int position) {
        tvBottom.setText(item.text);
    }

    public static Data data() {
        return data("---  到底啦  ---");
    }

    public static Data data(String text) {
        return new Data(text);
    }

    protected static class Data {

        private String text;

        public Data() {
        }

        public Data(String text) {
            this.text = text;
        }
    }
}
