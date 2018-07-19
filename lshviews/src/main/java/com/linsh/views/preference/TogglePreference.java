package com.linsh.views.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.linsh.ImageViewHelper;
import com.linsh.LshViewUtil;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class TogglePreference extends Preference<TogglePreference.ToggleViewHelper> implements View.OnClickListener {


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
    protected ToggleViewHelper initDetailHelper() {
        ImageView detail = new ImageView(getContext());
        ToggleViewHelper detailHelper = new ToggleViewHelper(detail);
        detailHelper.setToggleImage(R.drawable.ic_toggle_open, R.drawable.ic_toggle_close);
        return detailHelper;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TogglePreference);
        boolean isToggleOn = typedArray.getBoolean(R.styleable.TogglePreference_toggleOn, false);

        ToggleViewHelper detail = detail();
        detail.setToggle(isToggleOn);
        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        detail().toggle();
    }

    public class ToggleViewHelper extends ImageViewHelper {

        public ToggleViewHelper(ImageView view) {
            super(view);
        }

        public void toggle() {
            mView.setSelected(!mView.isSelected());
        }

        public void setToggle(boolean isOn) {
            mView.setSelected(isOn);
        }

        public boolean isToggleOn() {
            return mView.isSelected();
        }

        public void setToggleImage(int onResId, int offResId) {
            setToggleImage(LshViewUtil.getDrawable(getContext(), onResId),
                    LshViewUtil.getDrawable(getContext(), offResId));
        }

        public void setToggleImage(Drawable onDrawable, Drawable offDrawable) {
            setImage(LshViewUtil.createSelectedSelector(onDrawable, offDrawable));
        }
    }
}
