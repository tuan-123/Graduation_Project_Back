package com.zsc.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/28 10:03
 */
@Setter
@Getter
public class CommentVo {
    private String userId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
}
