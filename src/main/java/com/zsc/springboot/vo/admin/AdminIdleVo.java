package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsc.springboot.vo.CommentVo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 16:49
 */
@Setter
@Getter
public class AdminIdleVo {
    private Long id;
    private String userId;
    private String school;
    private String title;
    private String describe;
    private Object[] tab;
    private BigDecimal price;
    private Integer num;
    private String phone;
    private Object[] photo;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
    private List<CommentVo> comments;
}
