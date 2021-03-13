package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/3 15:22
 */
@Setter
@Getter
public class HelpListVo {
    private long currentPage;
    private long total;
    private long pages;
    private List<HelpVo> helpVoList;
}
