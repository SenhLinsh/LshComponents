package com.linsh;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/13
 *    desc   :
 * </pre>
 */
public class ImageViewHelper extends ViewHelper<ImageView> implements IImageViewHelper {

    public ImageViewHelper(ImageView view) {
        super(view);
    }

    @Override
    public void setImage(Bitmap bm) {
        mView.setImageBitmap(bm);
    }

    @Override
    public void setImage(Drawable drawable) {
        mView.setImageDrawable(drawable);
    }

    @Override
    public void setImage(int resId) {
        mView.setImageResource(resId);
    }

    @Override
    public void setImageAlpha(int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setImageAlpha(alpha);
        } else {
            mView.setAlpha(alpha);
        }
    }
}
