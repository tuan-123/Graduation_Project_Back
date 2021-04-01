package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/4/1 16:42
 */
@Setter
@Getter
public class ExceptionLogListVo {

    private long currentPage;
    private long pagesize;
    private long total;
    private List<ExceptionLogBriefVo> exceptionLogBriefVos;
}
