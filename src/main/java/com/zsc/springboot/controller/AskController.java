package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.form.AskForm;
import com.zsc.springboot.service.AskService;
import com.zsc.springboot.util.ImgHandlerUtil;
import com.zsc.springboot.vo.AskListVo;
import com.zsc.springboot.vo.admin.AdminAskListVo;
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
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/ask")
public class AskController {

    @Autowired
    private AskService askService;

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @RequiresAuthentication
    @PostMapping("/imageUpload")
    @ApiOperation(value = " 图片上传",response = ServerResponse.class,httpMethod = "POST")
    public ServerResponse updateImage(HttpServletRequest request, MultipartFile file, String userId){
        String imgUrl = ImgHandlerUtil.handleImg(request,file,userId);
        return ServerResponse.success(imgUrl);
    }

    @RequiresAuthentication
    @PostMapping("/askIssue")
    @ApiOperation(value = "帮问发布",response = ServerResponse.class,httpMethod = "POST")
    public ServerResponse askIssue(@Validated @RequestBody AskForm askForm){
        Integer result = askService.askIssue(askForm.getUserId(),askForm.getContent(),askForm.getPhoto());
        if(result != 1){
            return ServerResponse.fail("发布失败");
        }
        rabbitMessagingTemplate.convertAndSend("direct_exchange_orders","askCount","");
        return ServerResponse.success(null);
    }

    @RequiresAuthentication
    @GetMapping("/getAskList")
    @ApiOperation(value = "获取列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getAskList(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        AskListVo askListVo;
        if(date == null){
            askListVo = askService.getAskList(userId, query, currentPage, pageSize);
        }else{
            askListVo = askService.loadingMoreAskList(userId,query,currentPage,pageSize,date);
        }
        if(askListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(askListVo);
    }

    @RequiresAuthentication
    @GetMapping("/getAskListByUserId")
    @ApiOperation(value = "根据用户Id获取列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getAskListByUserId(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        AskListVo askListVo;
        if(date == null){
            askListVo = askService.getAskListByUserId(userId, query, currentPage, pageSize);
        }else {
            askListVo = askService.loadingMoreAskListByUserId(userId,query,currentPage,pageSize,date);
        }
        if(askListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(askListVo);
    }

    @RequiresAuthentication
    @GetMapping("/deleteAskById")
    @ApiOperation(value = "根据id删除",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse deleteAskById(@RequestParam("id")Long id){
        Integer deleted = askService.deleteAskById(id);
        if(deleted == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }

    /*
    @RequiresAuthentication
    @GetMapping("/loadingMoreAskListByUserId")
    @ApiOperation(value = "根据用户Id加载更多列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse loadingMoreAskListByUserId(@RequestParam("userId") String userId, @RequestParam("query") String query, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss") Date date){
        AskListVo askListVo = askService.loadingMoreAskListByUserId(userId, query, currentPage, pageSize,date);
        if(askListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(askListVo);
    }
     */

    /*
    @RequiresAuthentication
    @GetMapping("/loadingMoreAskList")
    @ApiOperation(value = "加载更多列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse loadingMoreAskList(@RequestParam("userId") String userId, @RequestParam("query") String query, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss") Date date){
        AskListVo askListVo = askService.loadingMoreAskList(userId, query, currentPage, pageSize,date);
        if(askListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(askListVo);
    }
     */

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @GetMapping("/admin/getAskList")
    @ApiOperation(value = "管理员获取ask列表",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse adminGetAskList(@RequestParam("query")String query,@RequestParam("pageNum")long pageNum,@RequestParam("pageSize")long pageSize){
        AdminAskListVo adminAskListVo = askService.adminGetAskList(query, pageNum, pageSize);
        return ServerResponse.success(adminAskListVo);
    }

    @RequiresAuthentication
    @GetMapping("/admin/getAskById")
    @RequiresRoles({"2"})
    @ApiOperation(value = "根据id获取ask详细信息",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse adminGetAskById(@RequestParam("id") Long id){
        return ServerResponse.success(askService.getAskByAskId(id));
    }

    @OperLogAnnotation(operModul = "提问管理",operType = "删除",operDesc = "删除提问")
    @RequiresAuthentication
    @GetMapping("/admin/deleteAskById")
    @ApiOperation(value = "管理员根据id删除",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse adminDeleteAskById(@RequestParam("id")Long id){
        Integer deleted = askService.deleteAskById(id);
        if(deleted == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }

}

