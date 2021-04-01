package com.zsc.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/21 16:58
 */
@Setter
@Getter
public class CarouselMapVo {
    private Integer id;
    private String img;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GTM+8")
    private Date createTime;
}
