package com.gc.sam.banner.looper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by xiaote on 2021/1/21
 * Description :
 **/

public class NoBugViewPager extends ViewPager {
    public NoBugViewPager(@NonNull Context context) {
        super(context);
    }

    public NoBugViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /**
         * 设ViewPager中有3张照片
         * 直到完全隐藏此ViewPager，并执行了onDetachedFromWindow
         * 再回来时，将会出现bug，第一次滑动时没有动画效果，并且，经常出现view没有加载的情况
         */
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);

            setCurrentItem(getCurrentItem());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
