package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/16 15:46
 */
@Getter
@Setter
public class SchoolVo {
    private Integer value;//id
    private String text;// name
    private String parentId;
}
