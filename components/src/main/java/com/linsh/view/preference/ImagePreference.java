package com.linsh.view.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.linsh.views.R;

import androidx.annotation.Nullable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class ImagePreference extends Preference<ImageView> {


    public ImagePreference(Context context) {
        super(context);
    }

    public ImagePreference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ImageView initDetailHelper() {
        ImageView detail = new ImageView(getContext());
        return detail;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImagePreference);
        int imageId = typedArray.getResourceId(R.styleable.ImagePreference_imageSrc, 0);

        ImageView detail = detail();
        if (imageId != 0) {
            detail.setImageResource(imageId);
        }
        typedArray.recycle();
    }
}
