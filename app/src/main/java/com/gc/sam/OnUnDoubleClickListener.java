package com.gc.sam;

import android.view.View;

import java.util.Calendar;

/**
 * @author: geyuecang
 * @time: 2022/1/5 11:47 上午
 * @des:
 **/
public abstract class OnUnDoubleClickListener implements View.OnClickListener {
    /**
     * 延迟响应时间 默认800毫秒
     */
    public static final int CLICK_DELAY = 800;
    /**
     * 延迟响应时间 默认800毫秒
     */
    private int mClickDelay;

    private long lastClickTime = 0;

    /**
     * @param delayTime 延迟响应的时间
     */
    public OnUnDoubleClickListener(int delayTime) {
        this.mClickDelay = delayTime;
    }

    public OnUnDoubleClickListener() {
        this.mClickDelay = CLICK_DELAY;
    }

    /**
     * 实例化时重写该方法
     *
     * @param v 原onClick方法参数
     */
    public abstract void onUnDoubleClick(View v);

    /**
     * @param v
     */
    @Override
    public final void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        // 如果点击过快则不执行
        if (currentTime - lastClickTime > mClickDelay) {
            lastClickTime = currentTime;
            onUnDoubleClick(v);
        }
    }
}
