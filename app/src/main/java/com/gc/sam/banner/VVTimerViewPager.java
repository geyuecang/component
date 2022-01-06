package com.gc.sam.banner;

import android.os.Handler;
import android.os.Message;

import com.gc.sam.banner.looper.LooperView;

import java.lang.ref.WeakReference;

/**
 * 设置轮播时间
 */
public class VVTimerViewPager extends Handler {

    private static final int LOOP_WHAT = 500101;

    private  int TIME_MILLIS = 3000;

    private WeakReference<LooperView> mWeakReference;

    public VVTimerViewPager(LooperView loopViewPager) {
        mWeakReference = new WeakReference<>(loopViewPager);
    }
    public VVTimerViewPager(LooperView loopViewPager, int time) {
        mWeakReference = new WeakReference<>(loopViewPager);
        this.TIME_MILLIS=time;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        LooperView viewPager = mWeakReference.get();
        if (viewPager == null) {
            return;
        }
        if (msg.what == LOOP_WHAT) {
            try {
                viewPager.nextPager();
                removeMessages(LOOP_WHAT);
                sendEmptyMessageDelayed(LOOP_WHAT, TIME_MILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startLoop() {
        if (hasMessages(LOOP_WHAT)) {
            return;
        }
        removeMessages(LOOP_WHAT);
        sendEmptyMessageDelayed(LOOP_WHAT, TIME_MILLIS);
    }

    public void stopLoop() {
        if (hasMessages(LOOP_WHAT)) {
            removeMessages(LOOP_WHAT);
        }
    }
}
