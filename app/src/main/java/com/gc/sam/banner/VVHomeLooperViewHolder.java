package com.gc.sam.banner;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gc.sam.OnUnDoubleClickListener;
import com.gc.sam.banner.looper.LooperViewHolder;

/**
 * bannerHolder实现类
 */
public class VVHomeLooperViewHolder implements LooperViewHolder {

    private Context context;

    public VVHomeLooperViewHolder(Context context, OnBannerItemClickListener listener) {
        this.onBannerItemClickListener = listener;
        this.context = context;
    }

    @Override
    public View createView(ViewGroup container) {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void bindView(View view, final Object data, Object umsData, int position) {
        final BannerDTO banner = (BannerDTO) umsData;
        final ImageView imageView = (ImageView) view;
        Glide.with(context)
                .load(banner.getImg_url())
                .into(imageView);

        imageView.setOnClickListener(new OnUnDoubleClickListener() {
            @Override
            public void onUnDoubleClick(View v) {
                //TODO banner 点击跳转
                if (!TextUtils.isEmpty(banner.getJump_url())) {
                    if (context instanceof Activity) {
                        //StartPath.start((Activity) context,banner.getJump_url());
                    }
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.onBannerItem(banner);
                    }
                }
            }
        });
    }

    public interface OnBannerItemClickListener {
        void onBannerItem(BannerDTO banner);
    }

    public OnBannerItemClickListener onBannerItemClickListener;

}
