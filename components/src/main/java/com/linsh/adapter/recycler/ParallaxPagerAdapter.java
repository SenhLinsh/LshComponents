package com.linsh.adapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import com.linsh.views.R;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/01/09
 *    desc   :
 * </pre>
 */
public abstract class ParallaxPagerAdapter extends PagerAdapter {

    public ParallaxPagerAdapter(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < viewPager.getChildCount(); i++) {
                    View childView = viewPager.getChildAt(i);
                    Object tag = childView.getTag(R.id.tag_view_holder);
                    if (tag != null && tag instanceof ViewHolder) {
                        ViewHolder viewHolder = (ViewHolder) tag;
                        int curPosition = (int) childView.getTag(R.id.tag_position);
                        float offset = (-positionOffset + curPosition - position);
                        viewHolder.setOffset(offset);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder = onCreateViewHolder(container, position);
        if (viewHolder != null) {
            onBindViewHolder(viewHolder, position);
            container.addView(viewHolder.itemView);
            viewHolder.itemView.setTag(this);
            viewHolder.itemView.setTag(R.id.tag_position, position);
            return viewHolder.itemView;
        }
        return null;
    }

    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int position);

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    public static abstract class ViewHolder {
        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        /**
         * 根据每个 page 的偏移设置子 View 的偏移, 以达到视差效果
         *
         * @param offset 当前 page 的偏移, 位于正中为 0, 位于左侧为 -1, 位于右侧为 1;
         *               范围为: -(显示页数+缓存页数-1)/2 ~  (显示页数+缓存页数-1)/2
         */
        public abstract void setOffset(float offset);
    }
}
