package com.linsh.views.album;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linsh.views.R;

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
public class AlbumSelectView extends BaseAlbumView<SelectableImage> implements AdapterView.OnItemClickListener {

    private int selectedCount;
    private int selectedLimit;

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
        SelectableImage image = mAdapter.getData().get(position);
        boolean selected = image.isSelected();
        if (selected) {
            if (selectedCount >= selectedLimit) {
                Toast.makeText(getContext(), String.format("最多能选 %d 张", selectedLimit), Toast.LENGTH_SHORT).show();
                return;
            }
            selectedCount++;
        } else {
            selectedCount--;
        }
        image.setSelected(!selected);
        mAdapter.notifyDataSetChanged();
    }

    public List<SelectableImage> getSelectedPhotos() {
        ArrayList<SelectableImage> list = new ArrayList<>();
        for (SelectableImage selectableImage : mAdapter.getData()) {
            if (selectableImage.isSelected()) {
                list.add(selectableImage);
            }
        }
        return list;
    }

    @Override
    protected void setView(SelectableImage selectableImage, ViewHolder viewHolder, int position) {
        if (selectableImage.isSelected()) {
            viewHolder.vMask.setVisibility(View.VISIBLE);
            viewHolder.vMask.setBackgroundResource(R.drawable.ic_done_white);
        } else {
            viewHolder.vMask.setVisibility(View.GONE);
            viewHolder.vMask.setBackgroundColor(Color.TRANSPARENT);
        }
        selectableImage.setImage(viewHolder.ivPhoto);
    }
}
