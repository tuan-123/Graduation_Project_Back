package com.zsc.springboot.vo.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/17 14:07
 */
@Setter
@Getter
public class AdminUserListVo {
    private long total;
    private long currentPage;
    private List<AdminUserVo> userVoList;
}
