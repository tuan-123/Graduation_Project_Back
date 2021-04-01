package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 17:05
 */
@Setter
@Getter
public class AdminIdleListVo {
    private long currentPage;
    private long pageSize;
    private long total;
    private List<AdminIdleBriefVo> adminIdleBriefVoList;
}
