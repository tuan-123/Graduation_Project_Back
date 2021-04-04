package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/17 14:23
 */
@Getter
@Setter
public class AdminUserVo {
    private String userId;
    private String nickName;
    private String email;
    private String image;
    private String school;
    private String schoolNum;
    private Integer faceLogin;
    private Integer state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Date createTime;
}
