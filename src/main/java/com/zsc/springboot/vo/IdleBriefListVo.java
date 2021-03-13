package com.zsc.springboot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/26 20:39
 */
@Setter
@Getter
public class IdleBriefListVo {
    private long total;
    private long currentPage;
    private long pages;
    private List<IdleBriefVo> idleBriefList;
}
