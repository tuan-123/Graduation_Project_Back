package com.zsc.springboot.common;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/12 16:33
 */
@Getter
@Setter
public class SendEmailCode {
    private String email;
    private String code;
}
