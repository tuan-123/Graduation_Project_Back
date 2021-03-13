package com.zsc.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/26 16:28
 */
@Setter
@Getter
public class IdleDetailVo {
    private String title;
    private String describe;
    private Object[] tab;
    private BigDecimal price;
    private String phone;
    private Object[] photo;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
    private List<CommentVo> comments;
}
