package com.gc.sam;

import android.graphics.Paint;

/**
 * Created by linyuqiang on 2018/5/6.
 * 用画笔得到文字一些尺寸
 */
public class XesPaintTextUtil {


    /**
     * 用画笔得到文字高度
     *
     * @param paint
     * @return
     */
    public static float getTextHeitht(Paint paint) {
        float heightOut = -paint.ascent() + paint.descent();
        return heightOut;
    }

    /**
     * 用画笔得到文字底线
     *
     * @param heightOut
     * @param paint
     * @return
     */
    public static int getBaseline(float heightOut, Paint paint) {
        int baseline = (int) ((heightOut - (paint.descent() - paint.ascent())) / 2 - paint.ascent());
        return baseline;
    }
}
