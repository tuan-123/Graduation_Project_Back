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
 *@Since：2021/3/3 15:11
 */
@Setter
@Getter
public class HelpVo {
    private Long id;
    private String userId;
    private String userName;
    private String userImg;
    private String article;
    private String time;
    private String place;
    private String to;
    private BigDecimal fee;
    private String descr;
    private String phone;
    private Object[] photos;
    private Integer state;
    private String acceptUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
    private List<CommentVo> commentVoList;
}
