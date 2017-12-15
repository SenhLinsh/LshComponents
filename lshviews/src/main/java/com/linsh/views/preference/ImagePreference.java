package com.linsh.views.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.linsh.ImageViewHelper;
import com.linsh.views.R;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/07
 *    desc   :
 * </pre>
 */
public class ImagePreference extends Preference<ImageViewHelper> {


    public ImagePreference(Context context) {
        super(context);
    }

    public ImagePreference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ImageViewHelper initDetailHelper() {
        ImageView detail = new ImageView(getContext());
        ImageViewHelper detailHelper = new ImageViewHelper(detail);
        return detailHelper;
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImagePreference);
        int imageId = typedArray.getResourceId(R.styleable.ImagePreference_imageSrc, 0);

        ImageViewHelper detail = detail();
        if (imageId != 0) {
            detail.setImage(imageId);
        }
        typedArray.recycle();
    }
}
