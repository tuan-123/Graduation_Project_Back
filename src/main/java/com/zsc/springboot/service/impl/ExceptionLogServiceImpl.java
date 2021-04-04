package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.entity.ExceptionLog;
import com.zsc.springboot.mapper.ExceptionLogMapper;
import com.zsc.springboot.service.ExceptionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.admin.ExceptionLogBriefVo;
import com.zsc.springboot.vo.admin.ExceptionLogListVo;
import com.zsc.springboot.vo.admin.ExceptionLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-03-31
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Override
    public Integer insertExceptionLog(ExceptionLog exceptionLog) {
        return exceptionLogMapper.insert(exceptionLog);
    }

    @Override
    public ExceptionLogVo getExceptionLogById(Long id) {
        return exceptionLogMapper.getExceptionLogById(id);
    }

    @Override
    public ExceptionLogListVo getExceptionBriefList(String query, long pageNum, long pageSize, Date start, Date end) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");

        if(!query.isEmpty())
            queryWrapper.like("oper_user_id",query);

        if(start != null)
            queryWrapper.ge("create_time",start);

        if(end != null)
            queryWrapper.le("create_time",end);

        Page<ExceptionLog> iPage = new Page<>(pageNum,pageSize);
        Page<ExceptionLog> exceptionLogPage = (Page<ExceptionLog>) exceptionLogMapper.selectPage(iPage,queryWrapper);
        List<ExceptionLog> exceptionLogs = exceptionLogPage.getRecords();
        List<ExceptionLogBriefVo> exceptionLogBriefVos = new ArrayList<>();

        if(exceptionLogs != null) {
            ExceptionLogBriefVo exceptionLogBriefVo;
            for (ExceptionLog exceptionLog : exceptionLogs) {
                exceptionLogBriefVo = new ExceptionLogBriefVo();
                exceptionLogBriefVo.setId(exceptionLog.getExcId());
                exceptionLogBriefVo.setExcName(exceptionLog.getExcName());
                exceptionLogBriefVo.setOperUserId(exceptionLog.getOperUserId());
                exceptionLogBriefVo.setOperMethod(exceptionLog.getOperMethod());
                exceptionLogBriefVo.setOperUri(exceptionLog.getOperUri());
                exceptionLogBriefVo.setOperIp(exceptionLog.getOperIp());
                exceptionLogBriefVo.setCreateTime(exceptionLog.getCreateTime());
                exceptionLogBriefVos.add(exceptionLogBriefVo);
            }
        }

        ExceptionLogListVo exceptionLogListVo = new ExceptionLogListVo();
        exceptionLogListVo.setCurrentPage(exceptionLogPage.getCurrent());
        exceptionLogListVo.setPagesize(exceptionLogPage.getPages());
        exceptionLogListVo.setTotal(exceptionLogPage.getTotal());
        exceptionLogListVo.setExceptionLogBriefVos(exceptionLogBriefVos);

        return exceptionLogListVo;
    }
}
