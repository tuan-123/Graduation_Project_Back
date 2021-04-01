package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/4/1 15:29
 */
@Setter
@Getter
public class OperLogListVo {
    private long currentPage;
    private long pageSize;
    private long total;
    private List<OperLogBriefVo> operLogBriefVos;
}
