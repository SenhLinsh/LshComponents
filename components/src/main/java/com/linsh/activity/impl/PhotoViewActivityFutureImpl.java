package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.linsh.activity.PhotoViewActivityFuture;
import com.linsh.base.LshImage;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.adapter.ViewPagerAdapterEx;
import com.linsh.utilseverywhere.SystemUtils;
import com.linsh.views.R;

import java.io.File;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
class PhotoViewActivityFutureImpl extends ActivityFutureImpl implements PhotoViewActivityFuture {

    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_DISPLAY_ITEM_INDEX = "display_item_index";

    public PhotoViewActivityFutureImpl(Context context) {
        super(context);
        getIntent().subscribe(PhotoViewActivitySubscriber.class);
    }

    @Override
    public PhotoViewActivityFuture setPhotos(String... urls) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, urls);
        return this;
    }

    @Override
    public PhotoViewActivityFuture setPhotos(Integer... resources) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, resources);
        return this;
    }

    @Override
    public PhotoViewActivityFuture setPhotos(File... files) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, files);
        return this;
    }

    @Override
    public PhotoViewActivityFuture setDisplayItemIndex(int index) {
        getIntent().putExtra(EXTRA_DISPLAY_ITEM_INDEX, index);
        return this;
    }

    private static class PhotoViewActivitySubscriber implements ActivitySubscribe.OnCreate {

        private ComponentActivity activity;
        private IPhotoViewActivity iActivity;
        private PhotoViewAdapter adapter;

        @Override
        public void attach(Activity activity) {
            this.activity = (ComponentActivity) activity;
            iActivity = (IPhotoViewActivity) ((ComponentActivity) activity).getIActivity();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            ViewPager viewPager = new ViewPager(activity);
            viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            viewPager.setBackgroundColor(Color.BLACK);
            activity.setContentView(viewPager);

            if (activity != null) {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
            SystemUtils.setTranslucentStatusBar(activity, Color.BLACK);

            adapter = new PhotoViewAdapter();
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (iActivity != null) {
                        iActivity.onItemSelected(position, adapter.getData().get(position));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                Object photos = extras.get(EXTRA_PHOTOS);
                if (photos instanceof String[]) {
                    adapter.setData(Arrays.asList((String[]) photos));
                } else if (photos instanceof Integer[]) {
                    adapter.setData(Arrays.asList((Integer[]) photos));
                } else if (photos instanceof File[]) {
                    adapter.setData(Arrays.asList((File[]) photos));
                }
                if (iActivity != null && photos != null) {
                    iActivity.onPhotosReceived(adapter.getData());
                }
            }

            int curItem = activity.getIntent().getIntExtra(EXTRA_DISPLAY_ITEM_INDEX, 0);
            if (curItem > 0 && curItem < adapter.getData().size()) {
                viewPager.setCurrentItem(curItem);
            }
            if (iActivity != null && curItem < adapter.getData().size()) {
                iActivity.onItemSelected(0, adapter.getData().get(0));
            }
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
}
