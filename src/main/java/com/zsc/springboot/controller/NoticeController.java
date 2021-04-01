package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.form.admin.NoticeForm;
import com.zsc.springboot.form.admin.UpdateNoticeForm;
import com.zsc.springboot.service.NoticeService;
import com.zsc.springboot.vo.admin.AdminNoticeVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "添加公告",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/addNotice")
    public ServerResponse addNotice(@Validated @RequestBody NoticeForm noticeForm){
        Integer addNotice = noticeService.addNotice(noticeForm.getContent(),noticeForm.getAddress(),noticeForm.getStartTime(),noticeForm.getEndTime());
        if(addNotice == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("添加失败");
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "管理员获取公告",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/adminGetNotice")
    public ServerResponse adminGetNotice(){
        AdminNoticeVo adminGetNotice = noticeService.adminGetNotice();
        return ServerResponse.success(adminGetNotice);
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "删除公告",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/deleteNotice")
    public ServerResponse deleteNotice(@RequestParam("id")Integer id){
        Integer deleteNotice = noticeService.deleteNoticeById(id);
        if(deleteNotice == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "修改公告",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/updateNoticeById")
    public ServerResponse updateNoticeById(@Validated @RequestBody UpdateNoticeForm updateNoticeForm){
        Integer updateNoticeById = noticeService.updateNoticeById(updateNoticeForm.getId(),updateNoticeForm.getContent(),updateNoticeForm.getAddress(),updateNoticeForm.getStartTime(),updateNoticeForm.getEndTime());
        if(updateNoticeById == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("修改失败");
    }

    @RequiresAuthentication
    @ApiOperation(value = "获取公告",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getNotice")
    public ServerResponse getNotice(){
        return ServerResponse.success(noticeService.getNotice());
    }
}
