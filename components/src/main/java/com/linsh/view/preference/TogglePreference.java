package com.linsh.view.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.linsh.utilseverywhere.DrawableSelectors;
import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.components.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class TogglePreference extends Preference<TogglePreference.ToggleView> implements View.OnClickListener {


    public TogglePreference(Context context) {
        super(context);
    }

    public TogglePreference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initPreference() {
        super.initPreference();
        setOnClickListener(this);
    }

    @Override
    protected ToggleView initDetailHelper() {
        ToggleView detail = new ToggleView(getContext());
        detail.setToggleImage(R.drawable.ic_components_toggle_open, R.drawable.ic_components_toggle_close);
        return detail;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TogglePreference);
        boolean isToggleOn = typedArray.getBoolean(R.styleable.TogglePreference_toggleOn, false);

        ToggleView detail = detail();
        detail.setToggle(isToggleOn);
        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        detail().toggle();
    }

    public class ToggleView extends AppCompatImageView {

        public ToggleView(Context context) {
            super(context);
        }

        public void toggle() {
            setSelected(!isSelected());
        }

        public void setToggle(boolean isOn) {
            setSelected(isOn);
        }

        public boolean isToggleOn() {
            return isSelected();
        }

        public void setToggleImage(int onResId, int offResId) {
            setToggleImage(ResourceUtils.getDrawable(onResId),
                    ResourceUtils.getDrawable(offResId));
        }

        public void setToggleImage(Drawable onDrawable, Drawable offDrawable) {
            setImageDrawable(DrawableSelectors.selected(onDrawable, offDrawable));
        }
    }
}
