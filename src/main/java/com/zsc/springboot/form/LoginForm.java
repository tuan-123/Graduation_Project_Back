package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/17 23:04
 */
@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "手机号码不能为空")
    private String phone;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
