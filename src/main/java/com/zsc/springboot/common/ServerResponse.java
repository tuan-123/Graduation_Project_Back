package com.zsc.springboot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/12 16:12
 */
@Data
public class ServerResponse {

    private int code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    private ServerResponse(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int code, String msg, Object data, String token){
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.token = token;
    }

    // 操作成功
    public static ServerResponse success(Object data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(),data);
    }
    public static ServerResponse addSuccess(){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(),"添加成功");
    }
    public static ServerResponse loginSuccess(Object data, String token){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(),data,token);
    }

    //操作失败
    public static ServerResponse fail(String msg){
        return new ServerResponse(ResponseCode.FAIL.getCode(),msg);
    }
    public static ServerResponse nullData(){
        return new ServerResponse(ResponseCode.FAIL.getCode(),"查无数据");
    }


}
