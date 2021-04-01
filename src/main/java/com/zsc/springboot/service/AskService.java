package com.zsc.springboot.service;

import com.zsc.springboot.entity.Ask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.AskListVo;
import com.zsc.springboot.vo.AskVo;
import com.zsc.springboot.vo.admin.AdminAskListVo;
import com.zsc.springboot.vo.admin.AdminAskVo;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-01
 */
public interface AskService extends IService<Ask> {
    public Integer askIssue(String userId,String content,String[] photos);
    public AskListVo getAskListByUserId(String userId, String query, long currentPage, long pageSize);
    public AskListVo getAskList(String userId, String query, long currentPage, long pageSize);
    public Integer deleteAskById(Long id);
    public AskListVo loadingMoreAskListByUserId(String userId, String query, long currentPage, long pageSize, Date date);
    public AskListVo loadingMoreAskList(String userId, String query, long currentPage, long pageSize, Date date);
    long getCount();
    AdminAskListVo adminGetAskList(String query,long currentPage,long pageSize);
    AdminAskVo getAskByAskId(Long id);

}
