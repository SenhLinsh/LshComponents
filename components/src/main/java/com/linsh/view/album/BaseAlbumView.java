package com.linsh.view.album;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.linsh.components.R;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 相册展示控件的父类抽取, 可在其基础上进行扩展, 如可选择的相册展示控件
 * </pre>
 */
abstract class BaseAlbumView<T extends Image> extends GridView {

    protected int itemSize;
    protected CustomImageSelectAdapter mAdapter;

    public BaseAlbumView(Context context) {
        super(context);
        initParent();
    }

    public BaseAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParent();
    }

    public BaseAlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParent();
    }

    protected void initParent() {
        int suggestSize = dp2px(getContext(), 120);
        int widthPixels = getScreenWidth(getContext());
        int num = Math.round(widthPixels * 1f / suggestSize);
        itemSize = widthPixels / (num == 0 ? 1 : num);
        setNumColumns(num == 0 ? 1 : num);

        mAdapter = new CustomImageSelectAdapter();
        setAdapter(mAdapter);
    }

    public void setPhotos(List<? extends T> images) {
        mAdapter.setData(images);
        mAdapter.notifyDataSetChanged();
    }

    public List<? extends T> getPhotos() {
        return mAdapter.getData();
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    protected abstract void setView(T t, ViewHolder viewHolder, int position);

    protected class CustomImageSelectAdapter extends BaseAdapter {

        private List<? extends T> photos;

        @Override
        public int getCount() {
            return photos == null ? 0 : photos.size();
        }

        @Override
        public T getItem(int position) {
            return photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setData(List<? extends T> photos) {
            this.photos = photos;
        }

        public List<? extends T> getData() {
            return photos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo_select, null);
                viewHolder = new ViewHolder();
                viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo_select);
                viewHolder.vMask = convertView.findViewById(R.id.v_mask);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.ivPhoto.getLayoutParams().width = itemSize;
            viewHolder.ivPhoto.getLayoutParams().height = itemSize;
            viewHolder.vMask.getLayoutParams().width = itemSize;
            viewHolder.vMask.getLayoutParams().height = itemSize;

            setView(photos.get(position), viewHolder, position);
            return convertView;
        }
    }

    protected static class ViewHolder {
        ImageView ivPhoto;
        View vMask;
    }

    private static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }
}
