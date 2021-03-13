package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/1 11:28
 */
@Getter
@Setter
public class AskForm {
    @NotEmpty(message = "用户Id不能为空")
    private String userId;
    @NotEmpty(message = "提问内容不能为空")
    private String content;
    private String[] photo;
}
