package com.zsc.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/1 18:00
 */
@Getter
@Setter
public class AskVo {
    private Long id;
    private String userId;
    private String userImg;
    private String userName;
    private String content;
    private Object[] photos;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
    private Integer hasResolve;
    private List<CommentVo> commentVoList;
}
