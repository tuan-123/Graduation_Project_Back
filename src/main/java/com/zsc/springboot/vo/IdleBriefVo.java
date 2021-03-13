package com.zsc.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/26 16:42
 */
@Setter
@Getter
public class IdleBriefVo {
    private Long id;
    private String userId;
    private String title;
    private String describe;
    private Object[] tab;
    private Integer num;
    private BigDecimal price;
    private String state;
    private String photo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
}
