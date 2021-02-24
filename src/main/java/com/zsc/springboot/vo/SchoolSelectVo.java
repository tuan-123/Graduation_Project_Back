package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/1/16 14:45
 */
@Setter
@Getter
public class SchoolSelectVo {

    private Integer value;//id
    private String text;//name
    private String parentId;
    private List<SchoolVo> children;
}
