package com.gc.sam.banner;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.gc.sam.DensityUtil;
import com.gc.sam.EmptyUtils;
import com.gc.sam.R;
import com.gc.sam.XesViewUtils;
import com.gc.sam.banner.looper.LooperView;

import java.util.List;


/**
 * description: 首页Banner
 * banner 视图实现类
 */
public class VVHomeBannerView extends ConstraintLayout {

    private LooperView looperVpBanner;
    private VVTimerViewPager handler;
    private LooperCircleIndicator indicator;
    private boolean autoLoop = true;
    List<BannerDTO> bannerList;

    public VVHomeBannerView(Context context) {
        this(context, null);
    }

    public VVHomeBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VVHomeBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.vv_home_banner, this);
        looperVpBanner = inflate.findViewById(R.id.looper_vp_banner);
        indicator = inflate.findViewById(R.id.looper_indicator);
        looperVpBanner.setClipToOutline(true);
        final int roundCornnerDp = 16;
        looperVpBanner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int pixel = DensityUtil.dp2px(roundCornnerDp);
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), pixel);
            }
        });
        if (autoLoop) {
            handler = new VVTimerViewPager(looperVpBanner);
        }
        looperVpBanner.setLooperListener(looperListener);
    }

    /**
     * 绑定数据
     *
     * @param bannerList 广告实体
     */
    public void bindData(List<BannerDTO> bannerList, VVHomeLooperViewHolder.OnBannerItemClickListener listener) {
        if (bannerList == null) {
            return;
        }

        this.bannerList = bannerList;
        int size = EmptyUtils.size(bannerList);
        looperVpBanner.updateData(bannerList, new VVHomeLooperViewHolder(getContext(), listener));
        if (size > 1) {
            autoLoop = true;
            indicator.setVisibility(View.VISIBLE);
            indicator.setViewPager(looperVpBanner.getViewPager(), bannerList);
            indicator.initSelectedView(0);
        } else {
            autoLoop = false;
            indicator.setVisibility(GONE);
        }
        looperVpBanner.getViewPager().setPageMargin(DensityUtil.dp2px(10));
    }


    private void startLoop() {
        if (handler != null) {
            handler.startLoop();
        }
    }

    private void stopLoop() {
        if (handler != null) {
            handler.stopLoop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //处理循环
        if (autoLoop) {
            if (looperVpBanner.getRealTotalCount() > 1 && isAutoLoopEnable) {
                startLoop();
            } else {
                stopLoop();
            }
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (autoLoop) {
            stopLoop();
        }

    }

    LooperView.ILooperListener looperListener = new LooperView.ILooperListener() {
        @Override
        public void onSelected(int position, int total) {
            if (XesViewUtils.getPageStatus(mLifecycleOwner) != XesViewUtils.PAGE_STATUS_BACKGROUND) {
                showLog(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (autoLoop) {
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    stopLoop();
                } else {
                    startLoop();
                }
            }
        }
    };

    public void onPause() {
        stopLoop();
    }

    public void onResume() {
        startLoop();
    }

    public void showLog(int realPosition) {
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
    }

    private LifecycleOwner mLifecycleOwner;

    public void setLifecycleOwner(LifecycleOwner owner) {
        mLifecycleOwner = owner;
    }

    private boolean isAutoLoopEnable = false;

    public void setAutoLoopEnable(boolean enable) {
        isAutoLoopEnable = enable;
    }
}
