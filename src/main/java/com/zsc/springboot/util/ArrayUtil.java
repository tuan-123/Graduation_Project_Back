package com.zsc.springboot.util;
/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/26 14:45
 */
public class ArrayUtil {
    public static String arrayToString(Object[] objects){
        if(objects != null && objects.length > 0){
            StringBuffer sb = new StringBuffer();
            for(Object content : objects){
                sb.append(content);
                sb.append(";");
            }
            return sb.toString();
        }
        return "";
    }

    public static Object[] stringToObject(String content){
        Object[] objects = null;
        if(content != null && content.length() > 0){
            objects = content.split(";");
            return objects;
        }
        return objects;
    }
}
