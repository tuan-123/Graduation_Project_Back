package com.zsc.springboot.form.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/18 17:30
 */
@Setter
@Getter
public class AddUserForm {
    @NotEmpty(message = "手机号码不能为空")
    private String userId;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "邮箱不能为空")
    @Email
    private String email;
    private Integer role;
}
