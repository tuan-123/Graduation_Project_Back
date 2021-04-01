package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 18:25
 */
@Setter
@Getter
public class AdminHelpBriefVo {
    private Long id;
    private String userId;
    private String helpArticle;
    private Integer helpState;
    private Date createTime;
}
