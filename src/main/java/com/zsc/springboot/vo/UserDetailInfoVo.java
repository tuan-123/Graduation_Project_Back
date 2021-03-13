package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/19 15:52
 */
@Setter
@Getter
public class UserDetailInfoVo {
    private String image;
    private String nickName;
    private String email;
    private String schoolName;
    private String schoolNum;
    private Integer faceLogin;
}
