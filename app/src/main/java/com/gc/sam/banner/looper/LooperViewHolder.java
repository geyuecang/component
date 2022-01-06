package com.gc.sam.banner.looper;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yghysdr@163.com on 2020-03-25
 * description:
 */
public interface LooperViewHolder {

    View createView(ViewGroup container);

    void bindView(View view, Object data,Object umsData,int position);
}
