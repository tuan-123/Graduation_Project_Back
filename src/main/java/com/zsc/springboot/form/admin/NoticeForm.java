package com.zsc.springboot.form.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/19 22:25
 */
@Setter
@Getter
public class NoticeForm {
    @NotEmpty(message = "内容不能为空")
    private String content;
    private String address;
    @NotNull
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date startTime;
    @NotNull
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date endTime;
}
