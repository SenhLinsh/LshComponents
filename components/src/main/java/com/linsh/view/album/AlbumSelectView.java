package com.linsh.view.album;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linsh.components.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/08
 *    desc   : 可选择的相册控件, 可简单地展示多图片并长按进行选择
 * </pre>
 */
public class AlbumSelectView extends BaseAlbumView<Image> implements AdapterView.OnItemClickListener {

    private int mSelectedCount;
    private int mSelectedLimit = 9;
    private boolean[] mSelectArray;

    public AlbumSelectView(Context context) {
        super(context);
        init();
    }

    public AlbumSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlbumSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectArray[position]) {
            mSelectedCount--;
        } else {
            if (mSelectedCount >= mSelectedLimit) {
                Toast.makeText(getContext(), String.format("最多能选 %d 张", mSelectedLimit), Toast.LENGTH_SHORT).show();
                return;
            }
            mSelectedCount++;
        }
        mSelectArray[position] = !mSelectArray[position];
        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(mSelectArray[position], position);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setLimitSelectedNum(int limit) {
        this.mSelectedLimit = limit;
    }

    public boolean[] getSelectArray() {
        return mSelectArray;
    }

    public List<Image> getSelectedPhotos() {
        ArrayList<Image> list = new ArrayList<>();
        List<? extends Image> photos = getPhotos();
        for (int i = 0; i < photos.size(); i++) {
            if (mSelectArray[i]) {
                list.add(photos.get(i));
            }
        }
        return list;
    }

    @Override
    public void setPhotos(List<? extends Image> images) {
        this.setPhotos(images, null);
    }

    public void setPhotos(List<? extends Image> images, List<Integer> selectedIndexes) {
        mSelectedCount = 0;
        // 确保已选择图片不大于限制数目
        if (images != null && images.size() > 0) {
            mSelectArray = new boolean[images.size()];
            if (mSelectedLimit > 0 && selectedIndexes != null && selectedIndexes.size() > 0) {
                for (int i = 0; i < Math.min(selectedIndexes.size(), mSelectedLimit); i++) {
                    mSelectArray[selectedIndexes.get(i)] = true;
                }
            }
        } else {
            mSelectArray = null;
        }
        super.setPhotos(images);
    }

    @Override
    protected void setView(Image image, ViewHolder viewHolder, int position) {
        if (mSelectArray != null && mSelectArray[position]) {
            viewHolder.vMask.setVisibility(View.VISIBLE);
            viewHolder.vMask.setBackgroundResource(R.drawable.ic_components_done_white);
        } else if (viewHolder.vMask.getVisibility() != View.GONE) {
            viewHolder.vMask.setVisibility(View.GONE);
            viewHolder.vMask.setBackgroundColor(Color.TRANSPARENT);
        }
        image.setImage(viewHolder.ivPhoto);
    }

    private OnItemSelectedListener mOnItemSelectedListener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(boolean selected, int position);
    }
}
