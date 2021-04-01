package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 22:54
 */
@Setter
@Getter
public class ExceptionLogVo {
    private Long id;
    private String excRequParam;
    private String excName;
    private String excMessage;
    private String operUserId;
    private String operMethod;
    private String operUri;
    private String operIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
}
