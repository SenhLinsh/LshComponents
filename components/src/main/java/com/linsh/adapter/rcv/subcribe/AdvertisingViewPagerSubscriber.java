package com.linsh.adapter.rcv.subcribe;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.linsh.base.adapter.RcvAdapterSubscribe;
import com.linsh.lshutils.utils.IdUtilsEx;
import com.linsh.utilseverywhere.HandlerUtils;


/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/12/19
 *    desc   : 轮播逻辑处理
 * </pre>
 */
public class AdvertisingViewPagerSubscriber implements RcvAdapterSubscribe.OnViewAttachedToWindow, RcvAdapterSubscribe.OnViewDetachedFromWindow {

    private static final String TAG = "AdvertisingRcvSubscribe";
    private final int what = IdUtilsEx.generateId();
    private final ViewPager2 viewPager2;
    private RecyclerView.Adapter<?> adapter;
    private long interval = 5000;
    private final Runnable mRun = new Runnable() {
        @Override
        public void run() {
            if (adapter != null) {
                int itemCount = adapter.getItemCount();
                int position = viewPager2.getCurrentItem();
                if (position == itemCount - 1) {
                    viewPager2.setCurrentItem(0, position == 1);
                } else {
                    viewPager2.setCurrentItem(position + 1, true);
                }
            }
            HandlerUtils.resendMessage(what, mRun, interval);
        }
    };

    public AdvertisingViewPagerSubscriber(ViewPager2 viewPager) {
        this.viewPager2 = viewPager;
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                HandlerUtils.resendMessage(what, mRun, interval);
            }
        });
    }

    public AdvertisingViewPagerSubscriber(ViewPager2 viewPager2, long interval) {
        this(viewPager2);
        setInterval(interval);
    }

    public void setInterval(long interval) {
        this.interval = Math.max(interval, 100);
    }

    @Override
    public void attach(RecyclerView.Adapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        HandlerUtils.resendMessage(what, mRun, interval);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        HandlerUtils.removeMessages(what);
    }
}
