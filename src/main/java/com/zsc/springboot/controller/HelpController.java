package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.form.HelpForm;
import com.zsc.springboot.service.HelpService;
import com.zsc.springboot.util.ImgHandlerUtil;
import com.zsc.springboot.vo.HelpListVo;
import com.zsc.springboot.vo.admin.AdminHelpListVo;
import com.zsc.springboot.vo.admin.AdminHelpVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/help")
public class HelpController {

    @Autowired
    private HelpService helpService;
    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @ApiOperation(value = "图片上传",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/imageUpload")
    @RequiresAuthentication
    public ServerResponse updateImage(HttpServletRequest request, MultipartFile file, String userId){
        String imgUrl = ImgHandlerUtil.handleImg(request,file,userId);
        return ServerResponse.success(imgUrl);
    }

    @ApiOperation(value = "帮代发布",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/helpIssue")
    @RequiresAuthentication
    public ServerResponse helpIssue(@Validated @RequestBody HelpForm helpForm){
        Integer result = helpService.helpIssue(helpForm.getUserId(),
                helpForm.getHelpArticle(),
                helpForm.getHelpTime(),
                helpForm.getHelpPlace(),
                helpForm.getHelpTo(),
                helpForm.getHelpFee(),
                helpForm.getHelpDescr(),
                helpForm.getHelpPhone(),
                helpForm.getHelpPhoto());
        if(result == 1) {
            rabbitMessagingTemplate.convertAndSend("direct_exchange_orders","helpCount","");
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("发布失败");
    }

    @ApiOperation(value = "根据Id获取列表", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getHelpListByUserId")
    public ServerResponse getHelpListByUserId(@RequestParam("userId")String userId,@RequestParam("query")String query,@RequestParam("currentPage")long currentPage,@RequestParam("pageSize")long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        HelpListVo helpListVo;
        if(date == null){
            helpListVo = helpService.getHelpListByUserId(userId, query, currentPage, pageSize);
        }else {
            helpListVo = helpService.loadingMoreHelpListByUserId(userId, query, currentPage, pageSize,date);
        }
        return ServerResponse.success(helpListVo);
    }

    @ApiOperation(value = "获取列表", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getHelpList")
    public ServerResponse getHelpList(@RequestParam("userId")String userId,@RequestParam("query")String query,@RequestParam("currentPage")long currentPage,@RequestParam("pageSize")long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        HelpListVo helpListVo;
        if(date == null){
            helpListVo = helpService.getHelpList(userId, query, currentPage, pageSize);
        }else {
            helpListVo = helpService.loadingMoreHelpList(userId, query, currentPage, pageSize,date);
        }
        return ServerResponse.success(helpListVo);
    }

    @ApiOperation(value = "根据id删除", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/deleteHelpById")
    public ServerResponse deleteHelpById(@RequestParam("id")Long id){
        Integer deleted = helpService.deleteHelpById(id);
        if(deleted == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }

    /*
    @ApiOperation(value = "获取更多列表", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/loadingMoreHelpList")
    public ServerResponse loadingMoreHelpList(@RequestParam("userId")String userId, @RequestParam("query")String query, @RequestParam("currentPage")long currentPage, @RequestParam("pageSize")long pageSize, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss") Date date){
        HelpListVo helpListByUserId = helpService.loadingMoreHelpList(userId, query, currentPage, pageSize,date);
        if(helpListByUserId == null)
            return ServerResponse.fail("空空如也");
        return ServerResponse.success(helpListByUserId);
    }*/

    /*
    @ApiOperation(value = "根据Id获取列表", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/loadingMoreHelpListByUserId")
    public ServerResponse loadingMoreHelpListByUserId(@RequestParam("userId")String userId, @RequestParam("query")String query, @RequestParam("currentPage")long currentPage, @RequestParam("pageSize")long pageSize, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss") Date date){
        HelpListVo helpListByUserId = helpService.loadingMoreHelpListByUserId(userId, query, currentPage, pageSize,date);
        if(helpListByUserId == null)
            return ServerResponse.fail("空空如也");
        return ServerResponse.success(helpListByUserId);
    }*/

    @ApiOperation(value = "接单", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/acceptHelp")
    public ServerResponse acceptHelp(@RequestParam("id")Long id,@RequestParam("userId")String userId){
        Integer accept = helpService.acceptHelp(id,userId);
        if(accept == 1)
            return ServerResponse.success(null);
        else if(accept == 0)
            return ServerResponse.fail("订单已被接");
        return ServerResponse.fail("接单失败");
    }

    @ApiOperation(value = "我的接单", response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/GetAcceptedHelpByAcceptUserId")
    public ServerResponse getAcceptedHelpByAcceptUserId(@RequestParam("userId")String userId,@RequestParam("query")String query,@RequestParam("currentPage")long currentPage,@RequestParam("pageSize")long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        HelpListVo helpListVo;
        helpListVo = helpService.getAcceptedHelpByAcceptUserId(userId, query, currentPage, pageSize,date);
        return ServerResponse.success(helpListVo);
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @GetMapping("/admin/getHelpList")
    @ApiOperation(value = "管理员获取help列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse adminGetHelpList(@RequestParam("query")String query,@RequestParam("pageNum")long pageNum,@RequestParam("pageSize")long pageSize){
        AdminHelpListVo helpListVo = helpService.adminGetHelpList(query, pageNum, pageSize);
        return ServerResponse.success(helpListVo);
    }

    @RequiresAuthentication
    @GetMapping("/admin/getHelpById")
    @RequiresRoles({"2"})
    @ApiOperation(value = "根据id获取idle详细信息",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse adminGetIdleById(@RequestParam("id") Long id){
        AdminHelpVo adminHelpVo = helpService.adminGetHelpById(id);
        return ServerResponse.success(adminHelpVo);
    }

}

