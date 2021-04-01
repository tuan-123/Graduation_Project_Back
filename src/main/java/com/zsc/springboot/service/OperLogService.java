package com.zsc.springboot.service;

import com.zsc.springboot.entity.OperLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.admin.OperLogListVo;
import com.zsc.springboot.vo.admin.OperLogVo;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-30
 */
public interface OperLogService extends IService<OperLog> {

    Integer inserOperLog(OperLog operLog);
    OperLogVo getOperLogById(Long id);
    OperLogListVo getOperLogBriefList(String query, long pageNum, long pageSize, Date start, Date end);

}
