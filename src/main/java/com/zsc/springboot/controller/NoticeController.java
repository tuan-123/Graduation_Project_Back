package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.entity.Notice;
import com.zsc.springboot.form.admin.NoticeForm;
import com.zsc.springboot.form.admin.UpdateNoticeForm;
import com.zsc.springboot.service.NoticeService;
import com.zsc.springboot.vo.NoticeVo;
import com.zsc.springboot.vo.admin.AdminNoticeVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisTemplate redisTemplate;

    @OperLogAnnotation(operModul = "公告管理",operType = "添加",operDesc = "添加公告")
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

    @OperLogAnnotation(operModul = "公告管理",operType = "删除",operDesc = "删除公告")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "删除公告",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/deleteNotice")
    public ServerResponse deleteNotice(@RequestParam("id")Integer id){
        Integer deleteNotice = noticeService.deleteNoticeById(id);
        if(deleteNotice == 1) {
            redisTemplate.delete("notice");
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("删除失败");
    }

    @OperLogAnnotation(operModul = "公告管理",operType = "修改",operDesc = "修改公告")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "修改公告",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/updateNoticeById")
    public ServerResponse updateNoticeById(@Validated @RequestBody UpdateNoticeForm updateNoticeForm){
        Integer updateNoticeById = noticeService.updateNoticeById(updateNoticeForm.getId(),updateNoticeForm.getContent(),updateNoticeForm.getAddress(),updateNoticeForm.getStartTime(),updateNoticeForm.getEndTime());
        if(updateNoticeById == 1) {
            redisTemplate.delete("notice");
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("修改失败");
    }

    @RequiresAuthentication
    @ApiOperation(value = "获取公告",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getNotice")
    public ServerResponse getNotice(){
        NoticeVo noticeVo = (NoticeVo) redisTemplate.opsForValue().get("notice");
        if(noticeVo == null){
            NoticeVo noticeVo1 = noticeService.getNotice();
            redisTemplate.opsForValue().set("notice",noticeVo1,1, TimeUnit.HOURS);
            return ServerResponse.success(noticeVo1);
        }
        return ServerResponse.success(noticeVo);
    }
}

