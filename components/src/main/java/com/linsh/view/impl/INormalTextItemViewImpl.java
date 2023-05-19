package com.linsh.view.impl;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.view.item.ITextItemView;
import com.linsh.components.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/10/10
 *    desc   :
 * </pre>
 */
class INormalTextItemViewImpl implements ITextItemView {

    private final TextView textView;

    public INormalTextItemViewImpl(Context context) {
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = Dps.toPx(1);
        layoutParams.setMargins(margin, margin, margin, margin);
        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        int padding = ResourceUtils.getDimens(R.dimen.lsh_item_padding);
        textView.setPadding(padding, padding, padding, padding);
        int textSize = ResourceUtils.getDimens(R.dimen.lsh_item_text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    public void setText(CharSequence text) {
        textView.setText(text);
    }

    @NonNull
    @Override
    public View getView() {
        return textView;
    }
}
