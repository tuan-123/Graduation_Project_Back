package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/21 19:42
 */
@Setter
@Getter
public class UpdatePsdForm {
    @NotEmpty(message = "用户Id不能为空")
    private String userId;
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
