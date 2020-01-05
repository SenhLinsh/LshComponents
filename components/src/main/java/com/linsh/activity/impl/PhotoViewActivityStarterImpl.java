package com.linsh.activity.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.linsh.activity.PhotoViewActivityStarter;
import com.linsh.base.LshImage;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.adapter.ViewPagerAdapterEx;

import java.io.File;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public class PhotoViewActivityStarterImpl extends DefaultActivityStarterImpl implements PhotoViewActivityStarter {

    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_DISPLAY_ITEM_INDEX = "display_item_index";

    public PhotoViewActivityStarterImpl(Context context) {
        super(context);
        getIntent().subscribe(PhotoViewActivitySubscriber.class);
    }

    @Override
    public PhotoViewActivityStarter setPhotos(String... urls) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, urls);
        return this;
    }

    @Override
    public PhotoViewActivityStarter setPhotos(Integer... resources) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, resources);
        return this;
    }

    @Override
    public PhotoViewActivityStarter setPhotos(File... files) {
        getIntent().getIntent().putExtra(EXTRA_PHOTOS, files);
        return this;
    }

    @Override
    public PhotoViewActivityStarter setDisplayItemIndex(int index) {
        getIntent().putExtra(EXTRA_DISPLAY_ITEM_INDEX, index);
        return this;
    }

    private static class PhotoViewActivitySubscriber implements ActivitySubscribe.OnCreate {

        private Activity activity;

        @Override
        public void attach(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            ViewPager viewPager = new ViewPager(activity);
            viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            viewPager.setBackgroundColor(Color.BLACK);
            activity.setContentView(viewPager);

            PhotoViewAdapter adapter = new PhotoViewAdapter();
            viewPager.setAdapter(adapter);

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
            }

            int curItem = activity.getIntent().getIntExtra(EXTRA_DISPLAY_ITEM_INDEX, 0);
            if (curItem > 0 && curItem < adapter.getData().size()) {
                viewPager.setCurrentItem(curItem);
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
                LshImage.with((String) obj).load(photoView);
            } else if (obj instanceof Integer) {
                LshImage.with((Integer) obj).load(photoView);
            } else if (obj instanceof File) {
                LshImage.with((File) obj).load(photoView);
            }
            return photoView;
        }
    }
}
