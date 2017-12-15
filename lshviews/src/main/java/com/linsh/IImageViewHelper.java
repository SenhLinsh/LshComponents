package com.linsh;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/12/13
 *    desc   : ViewHelper 接口
 * </pre>
 */
public interface IImageViewHelper extends IViewHelper<ImageView> {

    void setImage(Bitmap bitmap);

    void setImage(Drawable drawable);

    void setImage(@DrawableRes int resId);

    void setImageAlpha(int alpha);
}
