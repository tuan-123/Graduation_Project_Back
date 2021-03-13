package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/28 11:18
 */
@Setter
@Getter
public class CommentForm {
    @NotNull(message = "所属Id不能为空")
    private Long parentId;
    @NotEmpty(message = "评论用户id不能为空")
    private String userId;
    @NotEmpty(message = "内容不能空")
    private String content;
}
