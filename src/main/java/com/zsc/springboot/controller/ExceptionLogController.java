package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.service.ExceptionLogService;
import com.zsc.springboot.vo.admin.ExceptionLogListVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-03-31
 */
@RestController
@RequestMapping("/exceptionLog")
public class ExceptionLogController {

    @Autowired
    private ExceptionLogService exceptionLogService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequiresRoles({"2"})
    @RequiresAuthentication
    @GetMapping("/admin/getExceptionLogBriefList")
    @ApiOperation(value = "获取异常日志列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getExceptionLogBriefList(@RequestParam("query")String query, @RequestParam("pageNum")long pageNum, @RequestParam("pageSize")long pageSize, @RequestParam(value = "startTime",required = false) String startTime, @RequestParam(value = "endTime",required = false)String endTime) throws ParseException {
        Date start,end;
        start = startTime != null ? simpleDateFormat.parse(startTime) : null;
        end = endTime != null ? simpleDateFormat.parse(endTime) : null;
        ExceptionLogListVo exceptionLogListVo = exceptionLogService.getExceptionBriefList(query, pageNum, pageSize, start, end);
        return ServerResponse.success(exceptionLogListVo);
    }

    @RequiresRoles({"2"})
    @RequiresAuthentication
    @GetMapping("/admin/getExceptionLogById")
    @ApiOperation(value = "根据id获取异常日志信息",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getExceptionLogById(@RequestParam("id")Long id){
        return ServerResponse.success(exceptionLogService.getExceptionLogById(id));
    }
}

