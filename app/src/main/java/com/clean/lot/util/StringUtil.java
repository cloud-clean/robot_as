package com.clean.lot.util;

public class StringUtil {

    public static boolean isNotEmpty(String str){
        if(str == null){
            return false;
        }
        str = str.trim();
        if("".equals(str)){
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String str){
       return !isNotEmpty(str);
    }
}
