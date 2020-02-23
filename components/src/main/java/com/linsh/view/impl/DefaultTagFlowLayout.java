package com.linsh.view.impl;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.UnitConverseUtils;
import com.linsh.utilseverywhere.interfaces.Consumer;
import com.linsh.utilseverywhere.interfaces.Convertible;
import com.linsh.utilseverywhere.tools.ShapeBuilder;
import com.linsh.view.OnItemClickListener;
import com.linsh.view.layout.ITagFlowLayout;
import com.linsh.views.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/23
 *    desc   :
 * </pre>
 */
public class DefaultTagFlowLayout implements ITagFlowLayout {

    private final FlowLayout flowLayout;
    private OnItemClickListener onItemClickListener;
    private int itemCount;
    private Consumer<View> consumer;

    public DefaultTagFlowLayout(Context context) {
        flowLayout = new FlowLayout(context);
    }

    @Override
    public void setTags(List<? extends CharSequence> tags) {
        setTags(tags, null);
    }

    @Override
    public <T> void setTags(List<T> tags, Convertible<T, CharSequence> convertible) {
        int size = tags == null ? 0 : tags.size();
        for (int i = 0; i < size; i++) {
            if (i < flowLayout.getChildCount()) {
                setText(i, convert(convertible, tags.get(i)));
            } else {
                generateTextView().setText(convert(convertible, tags.get(i)));
            }
        }
        if (size < itemCount) {
            for (int i = size; i < itemCount; i++) {
                flowLayout.getChildAt(i).setVisibility(View.GONE);
            }
        }
        itemCount = size;
    }

    @Override
    public void onTagClick(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void decorateTagView(Consumer<View> consumer) {
        this.consumer = consumer;
    }

    private <T> CharSequence convert(Convertible<T, CharSequence> convertible, T value) {
        if (convertible != null) {
            return convertible.convert(value);
        } else if (value instanceof CharSequence) {
            return (CharSequence) value;
        } else {
            return value == null ? null : value.toString();
        }
    }

    private void setText(int index, CharSequence text) {
        View child = flowLayout.getChildAt(index);
        if (child.getVisibility() != View.VISIBLE) {
            child.setVisibility(View.VISIBLE);
        }
        if (child instanceof TextView) {
            ((TextView) child).setText(text);
        }
    }

    private TextView generateTextView() {
        TextView textView = new TextView(flowLayout.getContext());
        int dp4 = UnitConverseUtils.dp2px(4);
        int dp8 = UnitConverseUtils.dp2px(8);
        textView.setPadding(dp8, dp4, dp8, dp4);
        GradientDrawable shape = new ShapeBuilder()
                .setCornerRadius(1000)
                .setStroke(Dps.toPx(1), 0xFF333333)
                .getShape();
        textView.setBackground(shape);
        textView.setTag(R.id.tag_item_view, flowLayout.getChildCount());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    Object position = v.getTag(R.id.tag_item_view);
                    if (position instanceof Integer) {
                        onItemClickListener.onItemClick(v, (Integer) position);
                    }
                }
            }
        });
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dp5 = Dps.toPx(5);
        params.setMargins(dp5, dp5, dp5, dp5);
        textView.setLayoutParams(params);
        flowLayout.addView(textView);
        consumer.accept(textView);
        return textView;
    }

    @NonNull
    @Override
    public View getView() {
        return flowLayout;
    }
}
