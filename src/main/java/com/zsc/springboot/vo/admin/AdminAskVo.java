package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsc.springboot.vo.CommentVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 16:44
 */
@Setter
@Getter
public class AdminAskVo {
    private Long id;
    private String userId;
    private String school;
    private String content;
    private Object[] photos;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;
    private List<CommentVo> commentVoList;
}
