package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 16:18
 */
@Setter
@Getter
public class AdminAskListVo {
    private long currentPage;
    private long pages;
    private long total;
    private List<AdminAskBriefVo> askVoList;
}
