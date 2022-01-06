package com.gc.sam.banner.looper;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.gc.sam.banner.BannerDTO;
import com.gc.sam.banner.VVHomeLooperViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * viewpager adapter
 */
public class LooperPagerAdapter extends PagerAdapter {

    private LinkedList<View> freeView = new LinkedList<>();
    private VVHomeLooperViewHolder viewHolder;
    private List<BannerDTO> list;


    public LooperPagerAdapter(List<BannerDTO> list, VVHomeLooperViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else if (list.size() == 1) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = freeView.poll();
        if (view == null) {
            view = viewHolder.createView(container);
        }
        container.addView(view);
        int realPosition = LooperView.getRealPosition(position, list.size());
        BannerDTO banner = list.get(realPosition);
        viewHolder.bindView(view, null, banner, realPosition);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        View view = (View) object;
        container.removeView(view);
        freeView.push(view);
    }

}