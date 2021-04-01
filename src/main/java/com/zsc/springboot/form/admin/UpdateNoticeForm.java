package com.zsc.springboot.form.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/20 15:35
 */
@Setter
@Getter
public class UpdateNoticeForm {
    @NotNull(message = "id不能为空")
    private Integer id;
    @NotEmpty(message = "内容不能为空")
    private String content;
    private String address;
    @NotNull(message = "时间不能为空")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date startTime;
    @NotNull(message = "时间不能为空")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date endTime;
}
