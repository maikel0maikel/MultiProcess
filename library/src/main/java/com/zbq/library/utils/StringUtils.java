package com.zbq.library.utils;

public class StringUtils {
    private StringUtils(){}
    public static boolean isEmpty(String src){
        return src == null || src.length() == 0;
    }
}
