package com.zsc.springboot.common;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/12 16:12
 */
public enum ResponseCode {

    SUCCESS(200,"操作成功"),
    FAIL(1001,"操作失败"),
    ERROR(500,"服务器出错");

    private final int code;
    private final String msg;

    ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }
    public String getMsg(){
        return this.msg;
    }
}
