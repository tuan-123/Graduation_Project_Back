package com.zsc.springboot.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.service.OperLogService;
import com.zsc.springboot.vo.admin.OperLogListVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/operLog")
public class OperLogController {

    @Autowired
    private OperLogService operLogService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequiresRoles({"2"})
    @RequiresAuthentication
    @GetMapping("/admin/getOperLogBriefList")
    @ApiOperation(value = "获取操作日志列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getOperLogBriefList(@RequestParam("query")String query, @RequestParam("pageNum")long pageNum, @RequestParam("pageSize")long pageSize,@RequestParam(value = "startTime",required = false)String startTime,@RequestParam(value = "endTime",required = false) String endTime) throws ParseException {
        Date start,end;
        start = startTime != null ? simpleDateFormat.parse(startTime) : null;
        end = endTime != null ? simpleDateFormat.parse(endTime) : null;
        OperLogListVo operLogListVo = operLogService.getOperLogBriefList(query, pageNum, pageSize, start, end);
        return ServerResponse.success(operLogListVo);
    }

    @RequiresRoles({"2"})
    @RequiresAuthentication
    @GetMapping("/admin/getOperLogById")
    @ApiOperation(value = "根据id获取日志信息",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getOperLogById(@RequestParam("id")Long id){
        return ServerResponse.success(operLogService.getOperLogById(id));
    }

}

