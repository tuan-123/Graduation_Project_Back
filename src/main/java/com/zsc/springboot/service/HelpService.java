package com.zsc.springboot.service;

import com.zsc.springboot.entity.Help;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.HelpListVo;
import com.zsc.springboot.vo.admin.AdminHelpListVo;
import com.zsc.springboot.vo.admin.AdminHelpVo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-02
 */
public interface HelpService extends IService<Help> {
    public Integer helpIssue(String userId, String article, String time, String place, String to, BigDecimal fee,String describe, String phone, String[] photo);
    public HelpListVo getHelpListByUserId(String userId,String query,long currentPage,long pageSize);
    public HelpListVo getHelpList(String userId,String query,long currentPage,long pageSize);
    public Integer deleteHelpById(Long id);
    public HelpListVo loadingMoreHelpList(String userId, String query, long currentPage, long pageSize, Date date);
    public HelpListVo loadingMoreHelpListByUserId(String userId, String query, long currentPage, long pageSize, Date date);
    public Integer acceptHelp(Long id,String userId);
    long getCount();
    HelpListVo getAcceptedHelpByAcceptUserId(String userId,String query,long currentPage,long pageSize,Date date);
    AdminHelpListVo adminGetHelpList(String query,long pageNum,long pageSize);
    AdminHelpVo adminGetHelpById(Long id);

    String getUserEmailByHelpId(Long id);
}
