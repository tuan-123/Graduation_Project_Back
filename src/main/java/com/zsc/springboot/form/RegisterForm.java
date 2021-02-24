package com.zsc.springboot.form;

import lombok.Data;

import javax.validation.constraints.*;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/16 21:23
 */
@Data
public class RegisterForm {
    @NotEmpty(message = "手机号不能为空")
    private String phone;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "非法邮箱")
    private String email;
    @NotEmpty(message = "验证码不能为空")
    private String code;

}
