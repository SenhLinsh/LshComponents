package com.linsh.view.impl;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.linsh.lshutils.utils.Dps;
import com.linsh.utilseverywhere.UnitConverseUtils;
import com.linsh.utilseverywhere.tools.DrawableSelectorBuilder;
import com.linsh.utilseverywhere.tools.ShapeBuilder;
import com.linsh.view.tag.ITagView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/25
 *    desc   :
 * </pre>
 */
class TagView extends AppCompatTextView implements ITagView {

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int dp4 = UnitConverseUtils.dp2px(4);
        int dp8 = UnitConverseUtils.dp2px(8);
        setPadding(dp8, dp4, dp8, dp4);
        GradientDrawable normalDrawable = new ShapeBuilder()
                .setCornerRadius(1000)
                .setStroke(Dps.toPx(1), 0xFF333333)
                .getShape();
        GradientDrawable selectedDrawable = new ShapeBuilder()
                .setCornerRadius(1000)
                .setColor(0xFF333333)
                .setStroke(Dps.toPx(1), 0xFF333333)
                .getShape();
        StateListDrawable selector = new DrawableSelectorBuilder()
                .setSelectedDrawable(selectedDrawable)
                .setOtherStateDrawable(normalDrawable)
                .getSelector();
        setBackground(selector);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dp5 = Dps.toPx(5);
        params.setMargins(dp5, dp5, dp5, dp5);
        setLayoutParams(params);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }
}
