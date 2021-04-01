package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 18:57
 */
@Setter
@Getter
public class AdminHelpListVo {
    private long currentPage;
    private long pageSize;
    private long total;
    private List<AdminHelpBriefVo> helpBriefVos;
}
