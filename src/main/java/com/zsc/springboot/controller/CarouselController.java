package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.service.CarouselService;
import com.zsc.springboot.util.ImgHandlerUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "添加轮播图",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/addCarouselMap")
    public ServerResponse addCarouselMap(HttpServletRequest request, MultipartFile file, String address,String userId){
        String imgUrl = ImgHandlerUtil.handleImg(request, file, userId);
        Integer addCarousel = carouselService.addCarousel(imgUrl,address);
        if(addCarousel == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("添加失败");
    }

    @RequiresAuthentication
    @RequiresRoles(value = {"2","0"},logical = Logical.OR)
    @ApiOperation(value = "获取轮播图列表",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getCarouselList")
    public ServerResponse addCarouselMap(){
        return ServerResponse.success(carouselService.getCarouselMapList());
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "删除轮播图",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/deleteCarousel")
    public ServerResponse deleteCarousel(@RequestParam("id")Integer id){
        Integer deleted = carouselService.deleteCarousel(id);
        if(deleted == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("删除失败");
    }
}

