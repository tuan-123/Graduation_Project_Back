package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 16:15
 */
@Setter
@Getter
public class AdminAskBriefVo {

    private Long id;
    private String userId;
    private String content;
    private Date createTime;
}
