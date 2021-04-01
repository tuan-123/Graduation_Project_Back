package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 17:06
 */
@Setter
@Getter
public class AdminIdleBriefVo {
    private Long id;
    private String userId;
    private String title;
    private Object[] tab;
    private BigDecimal price;
    private Integer num;
    private String phone;
    private Date createTime;
}
