package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 19:27
 */
@Setter
@Getter
public class OperLogVo {
    private Long id;
    private String modul;
    private String type;
    private String descr;
    private String requestParam;
    private String userId;
    private String method;
    private String uri;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
