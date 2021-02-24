package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/17 23:38
 */
@Getter
@Setter
public class UserVo {
    private String phone;
    private String nickName;
    private String email;
    private Integer role;
    private Integer state;
    private String image;
    private Integer faceLogin;
    private Date createTime;
}
