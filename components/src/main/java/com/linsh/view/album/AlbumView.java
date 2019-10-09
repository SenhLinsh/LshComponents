package com.linsh.view.album;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 相册展示控件, 可简单展示多个图片
 * </pre>
 */
public class AlbumView extends BaseAlbumView<Image> {

    public AlbumView(Context context) {
        super(context);
    }

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setView(Image image, ViewHolder viewHolder, int position) {
        image.setImage(viewHolder.ivPhoto);
    }
}
