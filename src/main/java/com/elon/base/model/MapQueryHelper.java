package com.elon.base.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map类数据查询辅助类。将数据库中两个字段查询结果组合成map，方面搜索
 *
 * @author neo
 * @since 2025-02-19
 */
@Getter
@Setter
public class MapQueryHelper<K, V>{
    private K key = null;
    private V value = null;

    /**
     * 将查询的结果列表转换为Map. 应用自身保证key是不重复的。
     *
     * @param dataList 数据列表
     * @param <K> key类型
     * @param <V> value类型
     * @return Map表
     */
    public static <K, V> Map<K, V> covert2Map(List<MapQueryHelper<K, V>> dataList) {
        Map<K, V> valueMap = new HashMap<>();
        for (MapQueryHelper<K, V> data :dataList) {
            MapQueryHelper<K, V> value = new MapQueryHelper<>();
            valueMap.put(data.key, data.value);
        }

        return valueMap;
    }
}
