package com.gc.sam;

import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 判空相关工具类
 * Created by Administrator on 2017/3/13.
 */

public class EmptyUtils {

    private EmptyUtils() {
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Collection && ( ((Collection) obj).isEmpty() || ((Collection) obj).size() == 0) ) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断一个集合是否为空
     *
     * @param collection
     * @return
     */
    private static <T> boolean isEmpty(Collection<T> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个集合是否不为空
     *
     * @param collection
     * @return
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }


    /**
     * 返回一个集合的size
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> int size(Collection<T> collection) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    public static <T> List<T> filterNullItem(List<T> actionList) {
        if (isEmpty(actionList)){
            return actionList;
        }
        List<T> result = new ArrayList<>();
        for (T item:actionList){
            if (item!=null){
                result.add(item);
            }
        }
        return result;
    }

}
