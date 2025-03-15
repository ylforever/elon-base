package com.elon.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * List工具类
 *
 * @author elon
 * @since 20220-08-26
 */
public class ListUtil {
    /**
     * 判断列表是否为空
     *
     * @param list 列表
     * @param <T> 泛型类型
     * @return true-为空; false-不为空
     * @author elon
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 拆分List. 按给定的size将list拆成多组
     *
     * @param list 列表
     * @param size 每组数组的元素数量
     * @param <T> 泛型
     * @return 分组后的列表
     * @author elon
     */
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        int groupNum = list.size() / size;
        int lastGroupSize = list.size() % size;

        List<List<T>> listGroups = new ArrayList<>();
        for (int i = 0; i < groupNum; ++i) {
            int fromIndex = i * size;
            int toIndex = (i + 1) * size;
            listGroups.add(list.subList(fromIndex, toIndex));
        }

        if (lastGroupSize != 0) {
            listGroups.add(list.subList(list.size()-lastGroupSize, list.size()));
        }

        return listGroups;
    }
}
