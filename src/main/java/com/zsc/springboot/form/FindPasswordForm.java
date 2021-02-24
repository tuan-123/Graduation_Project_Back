package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/21 22:03
 */
@Setter
@Getter
public class FindPasswordForm {
    @NotEmpty(message = "用户Id不能为空")
    private String userId;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @Email(message = "邮箱格式不正确")
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    @NotEmpty(message = "验证码不能为空")
    private String code;
}
