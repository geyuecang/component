package com.gc.sam;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ScrollingView;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class XesViewUtils {

    public static final int PAGE_STATUS_UNKNOWN = 1;
    public static final int PAGE_STATUS_FRONTGROUND = 2;
    public static final int PAGE_STATUS_BACKGROUND = 3;

    /**
     * 得到子view在他上面ViewGroup的位置
     *
     * @param child
     * @param group
     * @return
     */
    public static int[] getLoc(View child, ViewGroup group) {
        int[] loc = new int[2];
        int left = child.getLeft();
        int top = child.getTop();
        ViewParent parent = child.getParent();
        while (parent != null && parent != group) {
            if (parent instanceof ViewGroup) {
                ViewGroup group2 = (ViewGroup) parent;
                left += group2.getLeft();
                top += group2.getTop();
                parent = parent.getParent();
            } else {
                break;
            }
        }
        loc[0] = left;
        loc[1] = top;
        return loc;
    }

    /**
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        if (!("" + text).equals("" + textView.getText())) {
            textView.setText(text);
        }
    }

    /**
     * @param v
     * @param visibility
     */
    public static void setViewVisibility(View v, int visibility) {
        if (v != null && v.getVisibility() != visibility) {
            v.setVisibility(visibility);
        }
    }


    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        /**取在父布局的位置，取getLocationOnScreen水滴屏不准*/
        int[] location = new int[]{view.getLeft(), view.getTop()};
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    /**
     * @param activity
     * @return
     */
    public static boolean isFullScreen(final Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    /**
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        //noinspection SimplifiableIfStatement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow().getAttributes().flags &
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    /**
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isFitsSystemWindows(final Activity activity) {
        //noinspection SimplifiableIfStatement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            if (viewGroup == null || viewGroup.getChildCount() == 0) {
                return false;
            }
            return viewGroup.getChildAt(0).
                    getFitsSystemWindows();
        }

        return false;
    }

    public static int mScreenHeight;
    public static int mScreenWeight;

    /*public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
        int width = mRight - mLeft;
        int height = mBottom - mTop;
        if (width > 0 && height > 0) {
            r.set(0, 0, width, height);
            if (globalOffset != null) {
                globalOffset.set(-mScrollX, -mScrollY);
            }
            return mParent == null || mParent.getChildVisibleRect(this, r, globalOffset);
        }
        return false;
    }*/

    /**
     * @param v
     * @return
     */
    public static boolean isVisible(View v) {
        Rect mRect = null;

        if (mScreenHeight != 0) {
            mRect = new Rect(0, 0, mScreenWeight, mScreenHeight);
        } else {
            mRect = new Rect();
        }
        return v.getLocalVisibleRect(new Rect());
    }

    /**
     * @param context
     */
    public static void initView(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
        mScreenWeight = point.x;
    }

    /**
     * 是否被遮挡
     *
     * @param view
     * @return
     */
    public static boolean isViewCovered(final View view) {
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        if (!totalViewVisible)//if any part of the view is clipped by any of its parents,return true
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            if (currentParent.getVisibility() != View.VISIBLE)//if the parent of view is not visible,return true
                return true;

            int start = indexOfViewInParent(currentView, currentParent);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                Rect viewRect = new Rect();
                view.getGlobalVisibleRect(viewRect);
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                if (Rect.intersects(viewRect, otherViewRect))//if view intersects its older brother(covered),return true
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }

    /**
     * @param view
     * @param parent
     * @return
     */
    private static int indexOfViewInParent(View view, ViewGroup parent) {
        int index;
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == view)
                break;
        }
        return index;
    }

    /**
     * 设置圆角
     *
     * @param view
     * @param dp
     */
    public static void clipViewCornerByDp(View view, final int dp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setClipToOutline(true);
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int pixel = DensityUtil.dp2px(dp);
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), pixel);
                }
            });
        }
    }

    /**
     * @param view
     */
    public static void clipViewCircle(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setClipToOutline(true);
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        }
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PAGE_STATUS_UNKNOWN, PAGE_STATUS_FRONTGROUND, PAGE_STATUS_BACKGROUND})
    public @interface PAGE_STATUS {
    }

    /**
     * @param owner
     * @return
     */
    public static int getPageStatus(LifecycleOwner owner) {
        if (owner instanceof Fragment) {
            return ((Fragment) owner).getUserVisibleHint() ?
                    PAGE_STATUS_FRONTGROUND : PAGE_STATUS_BACKGROUND;
        } else if (owner instanceof androidx.fragment.app.Fragment) {
            return ((androidx.fragment.app.Fragment) owner).getUserVisibleHint() ?
                    PAGE_STATUS_FRONTGROUND : PAGE_STATUS_BACKGROUND;
        } else {
            return PAGE_STATUS_UNKNOWN;
        }
    }

    /**
     * @param context
     * @param colorId
     * @return
     */
    public static int getColor(@NonNull Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorId);
        }
        //noinspection deprecation
        return context.getResources().getColor(colorId);
    }

    /**
     * @param view
     * @return
     */
    public static int measureViewHeight(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        int childHeightSpec;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        if (p.height > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(p.height, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
        return view.getMeasuredHeight();
    }

    /**
     * @param listView
     * @param y
     */
    public static void scrollListBy(@NonNull AbsListView listView, int y) {
        if (Build.VERSION.SDK_INT >= 19) {
            // Call the framework version directly
            listView.scrollListBy(y);
        } else if (listView instanceof ListView) {
            // provide backport on earlier versions
            final int firstPosition = listView.getFirstVisiblePosition();
            if (firstPosition == ListView.INVALID_POSITION) {
                return;
            }

            //noinspection UnnecessaryLocalVariable
            final ViewGroup listGroup = listView;
            final View firstView = listGroup.getChildAt(0);
            if (firstView == null) {
                return;
            }

            final int newTop = firstView.getTop() - y;
            ((ListView) listView).setSelectionFromTop(firstPosition, newTop);
        } else {
            listView.smoothScrollBy(y, 0);
        }
    }

    /**
     * @param view
     * @return
     */
    public static boolean isScrollableView(View view) {
        return view instanceof AbsListView
                || view instanceof ScrollView
                || view instanceof ScrollingView
                || view instanceof WebView
                || view instanceof ViewPager
                || view instanceof NestedScrollingChild
                || view instanceof NestedScrollingParent;
    }


    //滚动边界

    /**
     * 判断内容是否可以刷新
     *
     * @param targetView 内容视图
     * @param touch      按压事件位置
     * @return 是否可以刷新
     */
    public static boolean canRefresh(@NonNull View targetView, PointF touch) {
        if (canScrollUp(targetView) && targetView.getVisibility() == View.VISIBLE) {
            return false;
        }
        //touch == null 时 canRefresh 不会动态递归搜索
        if (targetView instanceof ViewGroup && touch != null) {
            ViewGroup viewGroup = (ViewGroup) targetView;
            final int childCount = viewGroup.getChildCount();
            PointF point = new PointF();
            for (int i = childCount; i > 0; i--) {
                View child = viewGroup.getChildAt(i - 1);
                if (isTransformedTouchPointInView(viewGroup, child, touch.x, touch.y, point)) {
                    touch.offset(point.x, point.y);
                    boolean can = canRefresh(child, touch);
                    touch.offset(-point.x, -point.y);
                    return can;
                }
            }
        }
        return true;
    }

    /**
     * 判断内容视图是否可以加载更多
     *
     * @param targetView  内容视图
     * @param touch       按压事件位置
     * @param contentFull 内容是否填满页面 (未填满时，会通过canScrollUp自动判断)
     * @return 是否可以刷新
     */
    public static boolean canLoadMore(@NonNull View targetView, PointF touch, boolean contentFull) {
        if (canScrollDown(targetView) && targetView.getVisibility() == View.VISIBLE) {
            return false;
        }
        //touch == null 时 canLoadMore 不会动态递归搜索
        if (targetView instanceof ViewGroup && touch != null) {
            ViewGroup viewGroup = (ViewGroup) targetView;
            final int childCount = viewGroup.getChildCount();
            PointF point = new PointF();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                if (isTransformedTouchPointInView(viewGroup, child, touch.x, touch.y, point)) {
                    touch.offset(point.x, point.y);
                    boolean can = canLoadMore(child, touch, contentFull);
                    touch.offset(-point.x, -point.y);
                    return can;
                }
            }
        }
        return (contentFull || canScrollUp(targetView));
    }

//    public static boolean canScrollDown(View targetView, MotionEvent event) {
//        if (canScrollDown(targetView) && targetView.getVisibility() == View.VISIBLE) {
//            return true;
//        }
//        //event == null 时 canScrollDown 不会动态递归搜索
//        if (targetView instanceof ViewGroup && event != null) {
//            ViewGroup viewGroup = (ViewGroup) targetView;
//            final int childCount = viewGroup.getChildCount();
//            PointF point = new PointF();
//            for (int i = 0; i < childCount; i++) {
//                View child = viewGroup.getChildAt(i);
//                if (isTransformedTouchPointInView(viewGroup, child, event.getX(), event.getY(), point)) {
//                    event = MotionEvent.obtain(event);
//                    event.offsetLocation(point.x, point.y);
//                    return canScrollDown(child, event);
//                }
//            }
//        }
//        return false;
//    }

    /**
     * @param targetView
     * @return
     */
    public static boolean canScrollUp(@NonNull View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final ViewGroup viewGroup = (ViewGroup) targetView;
                final AbsListView absListView = (AbsListView) targetView;
                return viewGroup.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0
                        || viewGroup.getChildAt(0).getTop() < targetView.getPaddingTop());
            } else {
                return targetView.getScrollY() > 0;
            }
        } else {
            return targetView.canScrollVertically(-1);
        }
    }

    /**
     * @param targetView
     * @return
     */
    public static boolean canScrollDown(@NonNull View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final ViewGroup viewGroup = (ViewGroup) targetView;
                final AbsListView absListView = (AbsListView) targetView;
                final int childCount = viewGroup.getChildCount();
                return childCount > 0 && (absListView.getLastVisiblePosition() < childCount - 1
                        || viewGroup.getChildAt(childCount - 1).getBottom() > targetView.getPaddingBottom());
            } else {
                return targetView.getScrollY() < 0;
            }
        } else {
            return targetView.canScrollVertically(1);
        }
    }

    //</editor-fold>

    //<editor-fold desc="transform Point">

    /**
     * @param group
     * @param child
     * @param x
     * @param y
     * @param outLocalPoint
     * @return
     */
    public static boolean isTransformedTouchPointInView(@NonNull View group, @NonNull View child, float x, float y, PointF outLocalPoint) {
        if (child.getVisibility() != View.VISIBLE) {
            return false;
        }
        final float[] point = new float[2];
        point[0] = x;
        point[1] = y;
//        transformPointToViewLocal(group, child, point);
        point[0] += group.getScrollX() - child.getLeft();
        point[1] += group.getScrollY() - child.getTop();
//        final boolean isInView = pointInView(child, point[0], point[1], 0);
        final boolean isInView = point[0] >= 0 && point[1] >= 0
                && point[0] < (child.getWidth())
                && point[1] < ((child.getHeight()));
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0] - x, point[1] - y);
        }
        return isInView;
    }

//    public static boolean pointInView(View view, float localX, float localY, float slop) {
//        final float left = /*Math.max(view.getPaddingLeft(), 0)*/ - slop;
//        final float top = /*Math.max(view.getPaddingTop(), 0)*/ - slop;
//        final float width = view.getWidth()/* - Math.max(view.getPaddingLeft(), 0) - Math.max(view.getPaddingRight(), 0)*/;
//        final float height = view.getHeight()/* - Math.max(view.getPaddingTop(), 0) - Math.max(view.getPaddingBottom(), 0)*/;
//        return localX >= left && localY >= top && localX < ((width) + slop) &&
//                localY < ((height) + slop);
//    }

//    public static void transformPointToViewLocal(ViewGroup group, View child, float[] point) {
//        point[0] += group.getScrollX() - child.getLeft();
//        point[1] += group.getScrollY() - child.getTop();
//    }
    //</editor-fold>
    /**
     * 将View 按照指定比例显示
     */
    /**
     * 已宽度为参考，约束视图的宽高比
     *
     * @param targetView
     * @param aspect
     */
    public static void setViewApsectByWidth(final View targetView, final float aspect) {

        if (targetView == null || aspect <= 0) {
            return;
        }

        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setViewAspectByWidthReal(targetView, aspect);
            }
        });

        setViewAspectByWidthReal(targetView, aspect);

    }

    /**
     * 已宽度为参考，约束视图的宽高比
     *
     * @param targetView
     * @param aspect
     */
    private static void setViewAspectByWidthReal(View targetView, float aspect) {

        ViewGroup.LayoutParams lp = targetView.getLayoutParams();

        int width = targetView.getMeasuredWidth();
        int height = lp.height;

        if (width > 0 && height > 0) {
            int newHeight = (int) (width / aspect);

            if (newHeight != height) {

                lp.height = newHeight;
                targetView.setLayoutParams(lp);
            }
        }
    }

    /**
     * @param textView
     * @param contentWidth
     */
    public static void justify(TextView textView, float contentWidth) {
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();

        ArrayList<String> lineList = lineBreak(text, paint, contentWidth);

        textView.setText(TextUtils.join(" ", lineList).replaceFirst("\\s", ""));
    }

    /**
     * @param text
     * @param paint
     * @param contentWidth
     * @return
     */
    private static ArrayList<String> lineBreak(String text, Paint paint, float contentWidth) {
        String[] wordArray = text.split("\\s");
        ArrayList<String> lineList = new ArrayList<String>();
        String myText = "";

        for (String word : wordArray) {
            if (paint.measureText(myText + " " + word) <= contentWidth)
                myText = myText + " " + word;
            else {
                int totalSpacesToInsert = (int) ((contentWidth - paint.measureText(myText)) / paint.measureText(" "));
                lineList.add(justifyLine(myText, totalSpacesToInsert));
                myText = word;
            }
        }
        lineList.add(myText);
        return lineList;
    }

    /**
     * @param text
     * @param totalSpacesToInsert
     * @return
     */
    private static String justifyLine(String text, int totalSpacesToInsert) {
        String[] wordArray = text.split("\\s");
        String toAppend = " ";

        while ((totalSpacesToInsert) >= (wordArray.length - 1)) {
            toAppend = toAppend + " ";
            totalSpacesToInsert = totalSpacesToInsert - (wordArray.length - 1);
        }
        int i = 0;
        String justifiedText = "";
        for (String word : wordArray) {
            if (i < totalSpacesToInsert)
                justifiedText = justifiedText + word + " " + toAppend;

            else
                justifiedText = justifiedText + word + toAppend;

            i++;
        }

        return justifiedText;
    }

    /**
     * @param num
     * @param fontFace
     * @param textSize
     * @param textColor
     * @param stroke
     * @param strokeColor
     * @return
     */
    public static Bitmap createTextStroke(String num, Typeface fontFace, float textSize, int textColor, int stroke, int strokeColor) {
        Paint paintOut2 = new Paint();
        paintOut2.setAntiAlias(true);
        paintOut2.setTextSize(textSize);
        paintOut2.setStrokeWidth(1);
        paintOut2.setTypeface(fontFace);
        paintOut2.setFakeBoldText(true);

        float widthOut = paintOut2.measureText(num) + stroke * 2;
        float heightOut = XesPaintTextUtil.getTextHeitht(paintOut2);

        float textHeight = heightOut + stroke * 2;
        int baseline = XesPaintTextUtil.getBaseline(textHeight, paintOut2);

        int x, y;

        Bitmap bitmap = Bitmap.createBitmap((int) (widthOut), (int) textHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);

        paintOut2.setColor(textColor);
        canvas2.drawText(num, stroke, baseline, paintOut2);

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        paintOut2.setStyle(Paint.Style.FILL);
//        paintOut2.setColor(Color.CYAN);
//        canvas2.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paintOut2);

        paintOut2.setColor(strokeColor);
        long before = System.currentTimeMillis();
        for (int j = 0; j < pixels.length; j++) {
            int pixel = pixels[j];
            x = j % bitmap.getWidth();
            y = j / bitmap.getWidth();
            if (pixel == textColor) {
                canvas2.drawCircle(x, y, stroke, paintOut2);
            }
        }
//        paintOut2.setColor(bianColor);
//        canvas2.drawText(num, stroke, baseline, paintOut2);
        before = System.currentTimeMillis();
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int noStrokeCount = 0;
        for (int j = 0; j < pixels.length; j++) {
            int pixel = pixels[j];
            x = j % bitmap.getWidth();
            y = j / bitmap.getWidth();
            int index = y * bitmap.getWidth() + x;
            if (pixel != strokeColor) {
                noStrokeCount++;
                boolean isInnerX = false;
                boolean isInnerY = false;
                boolean left = false;
                boolean right = false;
                boolean top = false;
                boolean bottom = false;
                int range = (int) (textHeight / 2);
                for (int k = 0; k < range; k++) {
                    int newX = x - k;
                    if (newX <= 0 || newX >= bitmap.getWidth()) {
                        isInnerX = false;
                        break;
                    } else {
                        int pixel2 = bitmap.getPixel(newX, (int) y);
                        if (pixel2 == strokeColor) {
                            left = true;
                            break;
                        }
                    }
                }
                if (left) {
                    for (int k = 0; k < range; k++) {
                        int newX = x + k;
                        if (newX <= 0 || newX >= bitmap.getWidth()) {
                            isInnerX = false;
                            break;
                        } else {
                            int pixel2 = bitmap.getPixel(newX, (int) y);
                            if (pixel2 == strokeColor) {
                                right = true;
                                break;
                            }
                        }
                    }
                    if (right) {
                        isInnerX = true;
                        for (int k = 0; k < range; k++) {
                            int newY = y - k;
                            if (newY <= 0 || newY >= bitmap.getHeight()) {
                                isInnerY = false;
                                break;
                            } else {
                                int pixel2 = bitmap.getPixel((int) x, newY);
                                if (pixel2 == strokeColor) {
                                    top = true;
                                    break;
                                }
                            }
                        }
                        for (int k = 0; k < range; k++) {
                            int newY = y + k;
                            if (newY <= 0 || newY >= bitmap.getHeight()) {
                                isInnerY = false;
                                break;
                            } else {
                                int pixel2 = bitmap.getPixel((int) x, newY);
                                if (pixel2 == strokeColor) {
                                    bottom = true;
                                    break;
                                }
                            }
                        }
                        if (top && bottom) {
                            isInnerY = true;
                        }
                    }
                }
                if (isInnerX && isInnerY) {
                    paintOut2.setColor(strokeColor);
                    canvas2.drawPoint(x, y, paintOut2);
                }
            }
        }
        paintOut2.setColor(textColor);
        canvas2.drawText(num, stroke, baseline, paintOut2);
        return bitmap;
    }
}
