package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.entity.OperLog;
import com.zsc.springboot.mapper.OperLogMapper;
import com.zsc.springboot.service.OperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.admin.OperLogBriefVo;
import com.zsc.springboot.vo.admin.OperLogListVo;
import com.zsc.springboot.vo.admin.OperLogVo;
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
 * @since 2021-03-30
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

    @Autowired
    private OperLogMapper operLogMapper;

    @Override
    public Integer inserOperLog(OperLog operLog) {
        return operLogMapper.insert(operLog);
    }

    @Override
    public OperLogVo getOperLogById(Long id) {
        return operLogMapper.getOperLogById(id);
    }

    @Override
    public OperLogListVo getOperLogBriefList(String query, long pageNum, long pageSize, Date start, Date end) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");

        if(!query.isEmpty())
            queryWrapper.like("oper_desc",query);

        if(start != null)
            queryWrapper.ge("create_time",start);

        if(end != null){
            queryWrapper.le("create_time",end);
        }

        Page<OperLog> iPage = new Page<>(pageNum,pageSize);
        Page<OperLog> operLogPage = (Page<OperLog>) operLogMapper.selectPage(iPage,queryWrapper);
        List<OperLog> operLogs = operLogPage.getRecords();
        List<OperLogBriefVo> operLogBriefVos = new ArrayList<>();
        if(operLogs != null){
            OperLogBriefVo operLogBriefVo;
            for(OperLog operLog : operLogs){
                operLogBriefVo = new OperLogBriefVo();
                operLogBriefVo.setId(operLog.getOperId());
                operLogBriefVo.setModul(operLog.getOperModul());
                operLogBriefVo.setType(operLog.getOperType());
                operLogBriefVo.setDesc(operLog.getOperDesc());
                operLogBriefVo.setUserId(operLog.getOperUserId());
                operLogBriefVo.setIp(operLog.getOperIp());
                operLogBriefVo.setCreateTime(operLog.getCreateTime());
                operLogBriefVos.add(operLogBriefVo);
            }
        }

        OperLogListVo operLogListVo = new OperLogListVo();
        operLogListVo.setCurrentPage(operLogPage.getCurrent());
        operLogListVo.setPageSize(operLogPage.getPages());
        operLogListVo.setTotal(operLogPage.getTotal());
        operLogListVo.setOperLogBriefVos(operLogBriefVos);

        return operLogListVo;
    }
}
