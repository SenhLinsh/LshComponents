package com.linsh.activity.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.linsh.activity.IPhotoViewActivity;
import com.linsh.base.LshImage;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.components.R;
import com.linsh.lshutils.adapter.ViewPagerAdapterEx;
import com.linsh.utilseverywhere.SystemUtils;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/05/19
 *    desc   :
 * </pre>
 */
class PhotoViewActivityImpl extends IActivityViewImpl<IPhotoViewActivity.Presenter> implements IPhotoViewActivity.View, ActivitySubscribe.OnCreate {

    private PhotoViewAdapter<Object> adapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewPager = new HackyProblematicViewPager(getActivity());
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewPager.setBackgroundColor(Color.BLACK);
        getActivity().setContentView(viewPager);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        SystemUtils.setTranslucentStatusBar(getActivity(), Color.BLACK);

        adapter = new PhotoViewAdapter<>();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getPresenter().onItemSelected(position, adapter.getData().get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void setPhotos(List<Object> photos) {
        adapter.setData(photos);
    }

    @Override
    public void setPhotos(List<Object> photos, int selectedIndex) {
        adapter.setData(photos);
        if (selectedIndex >= 0 && selectedIndex < photos.size()) {
            viewPager.setCurrentItem(selectedIndex, false);
        }
    }

    @Override
    public void setItemSelected(int index) {
        if (index >= 0 && index < adapter.getData().size()) {
            viewPager.setCurrentItem(index);
        }
    }

    private static class PhotoViewAdapter<T> extends ViewPagerAdapterEx<T> {

        @Override
        protected View getView(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Object obj = getData().get(position);
            if (obj instanceof String) {
                LshImage.with((String) obj).error(R.drawable.ic_components_error_image).load(photoView);
            } else if (obj instanceof Integer) {
                LshImage.with((Integer) obj).error(R.drawable.ic_components_error_image).load(photoView);
            } else if (obj instanceof File) {
                LshImage.with((File) obj).error(R.drawable.ic_components_error_image).load(photoView);
            }
            return photoView;
        }
    }

    /**
     * 修复 PhotoView 在使用中发生崩溃的问题
     */
    private static class HackyProblematicViewPager extends ViewPager {

        public HackyProblematicViewPager(@NonNull Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
