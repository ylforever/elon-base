package com.elon.base.util;

import java.util.UUID;

/**
 * 字符串处理工具类
 *
 * @author neo
 * @since 2024-05-13
 */
public class StringUtil {
    /**
     * 判断字符串是否为空。 增加值为null的判断处理.
     *
     * @param str 字符串值
     * @return 判断结果
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 生成UUID字符串
     *
     * @return UUID
     */
    public static String generateUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}
