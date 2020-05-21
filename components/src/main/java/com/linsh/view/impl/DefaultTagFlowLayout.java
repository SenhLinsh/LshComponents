package com.linsh.view.impl;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.UnitConverseUtils;
import com.linsh.utilseverywhere.interfaces.Consumer;
import com.linsh.utilseverywhere.interfaces.Consumer2;
import com.linsh.utilseverywhere.interfaces.Convertible;
import com.linsh.utilseverywhere.tools.DrawableSelectorBuilder;
import com.linsh.utilseverywhere.tools.ShapeBuilder;
import com.linsh.view.OnItemClickListener;
import com.linsh.view.OnItemLongClickListener;
import com.linsh.view.tag.ITagFlowLayout;
import com.linsh.views.R;

import java.util.List;

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
    private OnItemLongClickListener onItemLongClickListener;
    private Consumer<View> generateAdapter;
    private Consumer2<View, Integer> bindAdapter;
    private int itemCount;

    public DefaultTagFlowLayout(Context context) {
        flowLayout = new FlowLayout(context);
    }

    @Override
    public ITagFlowLayout setTags(List<? extends CharSequence> tags) {
        return setTags(tags, null);
    }

    @Override
    public <T> ITagFlowLayout setTags(List<T> tags, Convertible<T, CharSequence> convertible) {
        int size = tags == null ? 0 : tags.size();
        for (int i = 0; i < size; i++) {
            if (i < flowLayout.getChildCount()) {
                setText(flowLayout.getChildAt(i), convert(convertible, tags.get(i)), i);
            } else {
                setText(generateTextView(), convert(convertible, tags.get(i)), i);
            }
        }
        if (size < itemCount) {
            for (int i = size; i < itemCount; i++) {
                flowLayout.getChildAt(i).setVisibility(View.GONE);
            }
        }
        itemCount = size;
        return this;
    }

    @Override
    public ITagFlowLayout setOnTagClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
        return this;
    }

    @Override
    public ITagFlowLayout setOnTagLongClickListener(OnItemLongClickListener listener) {
        onItemLongClickListener = listener;
        return this;
    }

    @Override
    public ITagFlowLayout adaptGenerateView(Consumer<View> consumer) {
        generateAdapter = consumer;
        return this;
    }

    @Override
    public ITagFlowLayout adaptBindView(Consumer2<View, Integer> consumer) {
        bindAdapter = consumer;
        return this;
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

    private void setText(View child, CharSequence text, int position) {
        if (child.getVisibility() != View.VISIBLE) {
            child.setVisibility(View.VISIBLE);
        }
        if (child instanceof TextView) {
            ((TextView) child).setText(text);
            if (bindAdapter != null) {
                bindAdapter.accept(child, position);
            }
        }
    }

    private TextView generateTextView() {
        TextView textView = new TextView(flowLayout.getContext());
        int dp4 = UnitConverseUtils.dp2px(4);
        int dp8 = UnitConverseUtils.dp2px(8);
        textView.setPadding(dp8, dp4, dp8, dp4);
        GradientDrawable normalDrawable = new ShapeBuilder()
                .setCornerRadius(1000)
                .setStroke(Dps.toPx(1), 0xFF333333)
                .getShape();
        GradientDrawable selectedDrawable = new ShapeBuilder()
                .setCornerRadius(1000)
                .setColor(0x66000000)
                .setStroke(Dps.toPx(1), 0xFF333333)
                .getShape();
        StateListDrawable selector = new DrawableSelectorBuilder()
                .setSelectedDrawable(selectedDrawable)
                .setOtherStateDrawable(normalDrawable)
                .getSelector();
        textView.setBackground(selector);
        textView.setTag(R.id.uee_tag_item_view, flowLayout.getChildCount());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    Object position = v.getTag(R.id.uee_tag_item_view);
                    if (position instanceof Integer) {
                        onItemClickListener.onItemClick(v, (Integer) position);
                    }
                }
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    Object position = v.getTag(R.id.uee_tag_item_view);
                    if (position instanceof Integer) {
                        onItemLongClickListener.onItemLongClick(v, (Integer) position);
                    }
                    return true;
                }
                return false;
            }
        });
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dp5 = Dps.toPx(5);
        params.setMargins(dp5, dp5, dp5, dp5);
        textView.setLayoutParams(params);
        flowLayout.addView(textView);
        if (generateAdapter != null) {
            generateAdapter.accept(textView);
        }
        return textView;
    }

    @NonNull
    @Override
    public View getView() {
        return flowLayout;
    }
}
