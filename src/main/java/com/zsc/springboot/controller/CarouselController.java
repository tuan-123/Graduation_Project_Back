package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.service.CarouselService;
import com.zsc.springboot.util.ImgHandlerUtil;
import com.zsc.springboot.vo.CarouselMapVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.List;
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
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private RedisTemplate redisTemplate;

    @OperLogAnnotation(operModul = "轮播图管理",operType = "添加",operDesc = "添加轮播图")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "添加轮播图",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/addCarouselMap")
    public ServerResponse addCarouselMap(HttpServletRequest request, MultipartFile file, String address,String userId){
        String imgUrl = ImgHandlerUtil.handleImg(request, file, userId);
        Integer addCarousel = carouselService.addCarousel(imgUrl,address);
        if(addCarousel == 1) {
            redisTemplate.delete("carouseMap");
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("添加失败");
    }

    @RequiresAuthentication
    @RequiresRoles(value = {"2","0"},logical = Logical.OR)
    @ApiOperation(value = "获取轮播图列表",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getCarouselList")
    public ServerResponse addCarouselMap(){
        List<CarouselMapVo> carouselMapVoList = (List<CarouselMapVo>) redisTemplate.opsForValue().get("carouseMap");
        if(carouselMapVoList == null){
            List<CarouselMapVo> carouselMapVoList1 = carouselService.getCarouselMapList();
            if(carouselMapVoList1 != null){
                redisTemplate.opsForValue().set("carouseMap",carouselMapVoList1,1, TimeUnit.HOURS);
            }
            return ServerResponse.success(carouselMapVoList1);
        }
        return ServerResponse.success(carouselMapVoList);
    }

    @OperLogAnnotation(operModul = "轮播图管理",operType = "删除",operDesc = "删除轮播图")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "删除轮播图",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/deleteCarousel")
    public ServerResponse deleteCarousel(@RequestParam("id")Integer id){
        Integer deleted = carouselService.deleteCarousel(id);
        if(deleted == 1) {
            redisTemplate.delete("carouseMap");
            return ServerResponse.success(null);
        }
        return ServerResponse.fail("删除失败");
    }
}

