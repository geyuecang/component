package com.gc.sam.banner.looper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gc.sam.banner.BannerDTO;
import com.gc.sam.banner.VVHomeLooperViewHolder;

import java.util.List;

/**
 * Created by yghysdr@163.com on 2020-03-25
 * description: 循环ViewPager包装
 */
public class LooperView extends FrameLayout {

    public static final int MID_OFFSET = Integer.MAX_VALUE / 2;

    private ILooperListener looperListener;
    private List list;
    private NoBugViewPager viewPager;

    public LooperView(@NonNull Context context) {
        this(context, null);
    }

    public LooperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LooperView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateData(List<BannerDTO> list, VVHomeLooperViewHolder viewHolder) {
        this.list = list;
        viewPager = new NoBugViewPager(getContext());
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        LooperPagerAdapter looperPagerAdapter = new LooperPagerAdapter(list, viewHolder);
        viewPager.setAdapter(looperPagerAdapter);
        viewPager.setCurrentItem(MID_OFFSET);
        viewPager.addOnPageChangeListener(listener);
        removeAllViews();
        addView(viewPager);
        if (looperListener != null) {
            looperListener.onSelected((getRealPosition(MID_OFFSET, list.size())), list.size());
        }
    }


    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (looperListener != null) {
                looperListener.onSelected(getRealPosition(position, list.size()), list.size());
            }
        }

        private int preStatus = -1;
        @Override
        public void onPageScrollStateChanged(int state) {
            if (looperListener != null&& preStatus != state) {
                looperListener.onPageScrollStateChanged(state);
            }
            preStatus = state;
        }
    };

    public void setLooperListener(ILooperListener looperListener) {
        this.looperListener = looperListener;
    }

    public void nextPager() {
        if (viewPager != null) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    public static int getRealPosition(int position, int size) {
        int temp = (position - MID_OFFSET) % size;
        return temp < 0 ? temp + size : temp;
    }

    public int getRealTotalCount() {
        return list == null ? 0 : list.size();
    }

    public interface ILooperListener {
        void onSelected(int position, int total);

        void onPageScrollStateChanged(int state);

    }

    public ViewPager getViewPager() {
            return  viewPager;
    }
}
