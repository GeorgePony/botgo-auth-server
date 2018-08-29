package com.originaltek.botgo.user.util;

/**
 * create_time : 2018/8/23 11:28
 * author      : chen.zhangchao
 * todo        : 字符串操作类
 */
public abstract class StringUtil {

    public static boolean hasText(String text){

        if (!hasLength(text)) {
            return false;
        }
        int strLen = text.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }




}
