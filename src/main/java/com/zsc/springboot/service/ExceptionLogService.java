package com.zsc.springboot.service;

import com.zsc.springboot.entity.ExceptionLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.admin.ExceptionLogListVo;
import com.zsc.springboot.vo.admin.ExceptionLogVo;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-31
 */
public interface ExceptionLogService extends IService<ExceptionLog> {
    Integer insertExceptionLog(ExceptionLog exceptionLog);
    ExceptionLogVo getExceptionLogById(Long id);
    ExceptionLogListVo getExceptionBriefList(String query, long pageNum, long pageSize, Date start, Date end);
}
