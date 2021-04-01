package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.service.OperLogService;
import com.zsc.springboot.vo.admin.OperLogListVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequiresRoles({"2"})
    @RequiresAuthentication
    @GetMapping("/admin/getOperLogBriefList")
    @ApiOperation(value = "获取操作日志列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getOperLogBriefList(@RequestParam("query")String query, @RequestParam("pageNum")long pageNum, @RequestParam("pageSize")long pageSize, @RequestParam("startTime")Date start,@RequestParam("endTime")Date end){
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

