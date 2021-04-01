package com.zsc.springboot.service;

import com.zsc.springboot.entity.Idle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.form.IdleForm;
import com.zsc.springboot.vo.IdleBriefListVo;
import com.zsc.springboot.vo.IdleBriefVo;
import com.zsc.springboot.vo.IdleDetailVo;
import com.zsc.springboot.vo.admin.AdminAskVo;
import com.zsc.springboot.vo.admin.AdminIdleListVo;
import com.zsc.springboot.vo.admin.AdminIdleVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-02-26
 */
public interface IdleService extends IService<Idle> {
    public Integer idleIssue(IdleForm idleForm);
    public IdleDetailVo getIdleById(Long id);
    public IdleDetailVo getUpIdleById(Long id);
    public IdleBriefListVo getIdleBriefList(String userId, String query, long currentPage, long pageSize);
    public IdleBriefListVo getIdleBriefListByUserId(String userId, String query, long currentPage, long pageSize);
    public IdleBriefListVo loadingMoreIdleBriefListByUserId(String userId, String query, long currentPage, long pageSize, Date date);
    public IdleBriefListVo loadingMoreIdleBriefList(String userId, String query, long currentPage, long pageSize, Date date);
    public Integer downIdleById(Long id);
    public Integer deleteIdleById(Long id);
    public Integer upIdleById(Long id);
    long getCount();
    AdminIdleVo adminGetIdleById(Long id);
    AdminIdleListVo adminGetIdleList(String query, long pageNum, long pageSize);
}
