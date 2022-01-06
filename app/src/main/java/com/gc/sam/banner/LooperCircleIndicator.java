package com.gc.sam.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gc.sam.EmptyUtils;

import java.util.List;


/**
 * 适合com.xueersi.parentsmeeting.modules.contentcenter.home.banner.looper.LooperView使用的 CircleIndicator
 */
public class LooperCircleIndicator extends BaseCircleIndicator {
    public static final int MID_OFFSET = Integer.MAX_VALUE / 2;
    private ViewPager mViewpager;
    private List mList;
    public LooperCircleIndicator(Context context) {
        super(context);
    }

    public LooperCircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LooperCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LooperCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewPager(@Nullable ViewPager viewPager,List list) {
        mViewpager = viewPager;
        mList=list;
        if (mViewpager != null && mViewpager.getAdapter() != null&& EmptyUtils.isNotEmpty(list)) {
            mLastPosition = -1;
            createIndicators();
            mViewpager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewpager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewpager.getCurrentItem());
        }
    }

    private void createIndicators() {
        PagerAdapter adapter = mViewpager.getAdapter();
        int count;
        if (adapter == null) {
            count = 0;
        } else {
           // count = adapter.getCount();
            count= mList.size();
        }
        createIndicators(count, getRealPosition(mViewpager.getCurrentItem(),count));
    }


    public static int getRealPosition(int position, int size) {
        int temp = (position - MID_OFFSET) % size;
        return temp < 0 ? temp + size : temp;
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                    if (mViewpager.getAdapter() == null
                            || mViewpager.getAdapter().getCount() <= 0|| EmptyUtils.isEmpty(mList)) {
                        return;
                    }
                    animatePageSelected( getRealPosition(mViewpager.getCurrentItem(),mList.size()));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    /**
     * @deprecated User ViewPager addOnPageChangeListener
     */
    @Deprecated
    public void setOnPageChangeListener(
            ViewPager.OnPageChangeListener onPageChangeListener) {
        if (mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        mViewpager.removeOnPageChangeListener(onPageChangeListener);
        mViewpager.addOnPageChangeListener(onPageChangeListener);
    }
}
