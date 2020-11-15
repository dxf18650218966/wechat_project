package com.wechat.tool;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象工具
 * @Author dai
 * @Date 2020/1/29
 */
public class ObjectUtil {

    /**
     * 为空
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            CharSequence str = (CharSequence)obj;
            return str == null || str.length() == 0;
        } else if (obj instanceof Map) {
            Map map = (Map)obj;
            return null == map || map.isEmpty();
        } else if (obj instanceof Iterable) {
            Iterable iterable = (Iterable)obj;
            return null == iterable || isNull(iterable.iterator());
        } else if (obj instanceof Iterator) {
            Iterator iterator = (Iterator)obj;
            return null == iterator || !iterator.hasNext();
        } else {
            if(null == obj ? false : obj.getClass().isArray()){
                if (null == obj) {
                    return true;
                } else if (null == obj ? false : obj.getClass().isArray()) {
                    return 0 == Array.getLength(obj);
                }
                return false;
            }else{
                return false;
            }
        }
    }

    /**
     * 不为空
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 集合中，只要有一个为null,就返回true
     * @param list
     * @return
     */
    public static boolean hasNull(Object... list){
        for (Object obj : list){
            boolean res = isNull(obj);
            if(res){
                return true;
            }
        }
        return false;
    }


    /**
     * 将一个集合进行分割
     * @param collections 需要分割的集合
     * @param maxNum 集合最大的数量
     * @return 分割后的集合
     */
    public static <T> Collection<Collection<T>> subCollection(Collection<T> collections, int maxNum){
        // 计算切分次数
        int limit = (collections.size() + maxNum - 1);
        //获取分割后的集合
        Collection<Collection<T>> splitCollection = Stream.iterate(
                0, n -> n + 1).limit(limit).parallel().map(
                a -> collections.stream().skip(a * maxNum).limit(maxNum).parallel()
                        .collect(Collectors.toList())).collect(Collectors.toList());

        return splitCollection;
    }
}
