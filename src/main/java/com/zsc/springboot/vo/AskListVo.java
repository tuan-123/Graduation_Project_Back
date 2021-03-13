package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/1 18:06
 */
@Setter
@Getter
public class AskListVo {
    private long currentPage;
    private long pages;
    private long total;
    private List<AskVo> askVoList;
}
