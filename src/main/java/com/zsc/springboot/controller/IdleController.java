package com.zsc.springboot.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsc.springboot.common.SendEmailCode;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.entity.Idle;
import com.zsc.springboot.form.IdleForm;
import com.zsc.springboot.service.IdleService;
import com.zsc.springboot.service.impl.IdleServiceImpl;
import com.zsc.springboot.util.ArrayUtil;
import com.zsc.springboot.util.ImgHandlerUtil;
import com.zsc.springboot.vo.IdleBriefListVo;
import com.zsc.springboot.vo.IdleDetailVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/idle")
public class IdleController {

    @Autowired
    private IdleService idleService;

    @RequiresAuthentication
    @PostMapping("/imageUpload")
    @ApiOperation(value = " 图片上传",response = ServerResponse.class,httpMethod = "POST")
    public ServerResponse updateImage(HttpServletRequest request, MultipartFile file, String userId){
        String imgUrl = ImgHandlerUtil.handleImg(request,file,userId);
        return ServerResponse.success(imgUrl);
    }

    @ApiOperation(value = "闲置发布提交",response = ServerResponse.class,httpMethod = "POST")
    @RequiresAuthentication
    @PostMapping("/idleIssue")
    public ServerResponse idleIssue(@Validated @RequestBody IdleForm idleForm){
        Integer result = idleService.idleIssue(idleForm);
        if(result == 1){
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("发布失败");
    }

    @ApiOperation(value = "根据id获取闲置详细信息(上架商品)",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getDetailById")
    public ServerResponse getDetailById(@RequestParam("id") String id){
        IdleDetailVo idleDetailVo = idleService.getIdleById(Long.parseLong(id));
        if(idleDetailVo == null)
            return ServerResponse.fail("查询不到");
        return ServerResponse.success(idleDetailVo);
    }

    @ApiOperation(value = "根据id获取闲置详细信息(上架商品)",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getUpDetailById")
    public ServerResponse getUpDetailById(@RequestParam("id") String id){
        IdleDetailVo idleDetailVo = idleService.getUpIdleById(Long.parseLong(id));
        if(idleDetailVo == null)
            return ServerResponse.fail("商品已下架");
        return ServerResponse.success(idleDetailVo);
    }

    @ApiOperation(value = "获取闲置列表",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getIdleBriefList")
    public ServerResponse getIdleBriefList(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        IdleBriefListVo idleBriefListVo;
        if(date == null){
            idleBriefListVo = idleService.getIdleBriefList(userId,query,currentPage,pageSize);
        }else {
            idleBriefListVo = idleService.loadingMoreIdleBriefList(userId,query,currentPage,pageSize,date);
        }
        if(idleBriefListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(idleBriefListVo);
    }

    @ApiOperation(value = "根据用户id查询闲置列表",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/getIdleBriefListByUserId")
    public ServerResponse getIdleBriefListByUserId(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        IdleBriefListVo idleBriefListVo;
        if(date == null){
            idleBriefListVo = idleService.getIdleBriefListByUserId(userId,query,currentPage,pageSize);
        }else {
            idleBriefListVo = idleService.loadingMoreIdleBriefListByUserId(userId,query,currentPage,pageSize,date);
        }
        if(idleBriefListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(idleBriefListVo);
    }

    /*
    @ApiOperation(value = "根据用户id加载更多闲置列表",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/loadingMoreIdleBriefListByUserId")
    public ServerResponse loadingMoreIdleBriefListByUserId(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        IdleBriefListVo idleBriefListVo = idleService.loadingMoreIdleBriefListByUserId(userId,query,currentPage,pageSize,date);
        if(idleBriefListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(idleBriefListVo);
    }
     */

    /*
    @ApiOperation(value = "加载更多闲置列表",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/loadingMoreIdleBriefList")
    public ServerResponse loadingMoreIdleBriefList(@RequestParam("userId") String userId,@RequestParam("query") String query,@RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")Date date){
        IdleBriefListVo idleBriefListVo = idleService.loadingMoreIdleBriefList(userId,query,currentPage,pageSize,date);
        if(idleBriefListVo == null){
            return ServerResponse.fail("空空如也");
        }
        return ServerResponse.success(idleBriefListVo);
    }
     */

    @ApiOperation(value = "根据id下架商品",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/downIdleById")
    public ServerResponse downIdleById(@RequestParam("id")Long id){
        Integer result = idleService.downIdleById(id);
        if(result == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("下架失败");
    }

    @ApiOperation(value = "根据id上架商品",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/upIdleById")
    public ServerResponse upIdleById(@RequestParam("id")Long id){
        Integer result = idleService.upIdleById(id);
        if (result == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("上架失败");
    }

    @ApiOperation(value = "根据id删除商品",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/deleteIdleById")
    public ServerResponse deleteIdleById(@RequestParam("id")Long id){
        Integer result = idleService.deleteIdleById(id);
        if (result == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }
}

