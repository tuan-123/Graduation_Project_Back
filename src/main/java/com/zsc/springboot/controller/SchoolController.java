package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.service.SchoolService;
import com.zsc.springboot.service.impl.SchoolServiceImpl;
import com.zsc.springboot.vo.SchoolSelectVo;
import com.zsc.springboot.vo.SchoolVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加省份
     * @param provinceName
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @OperLogAnnotation(operModul = "学校管理",operType = "添加",operDesc = "添加省份")
    @PostMapping("/addProvince")
    public ServerResponse addProvince(@RequestParam("province") String provinceName){
        //System.out.println(provinceName);
        boolean addProvince = schoolService.addProvince(provinceName);
        return addProvince?ServerResponse.addSuccess():ServerResponse.fail("添加失败");
    }

    /**
     *  添加学校
     * @param provinceName  省份全称
     * @param schoolName   学校全称
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @OperLogAnnotation(operModul = "学校管理",operType = "添加",operDesc = "添加学校")
    @PostMapping("/addSchool")
    public ServerResponse addSchool(@RequestParam("provinceName") String provinceName,@RequestParam("schoolName") String schoolName){
        boolean addSchool = schoolService.addSchool(provinceName,schoolName);
        return addSchool?ServerResponse.addSuccess():ServerResponse.fail("添加失败");
    }

    @RequiresAuthentication
    @ApiOperation(value = "获取所有学校",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getAllSchool")
    public ServerResponse getAllSchool(){
        List<SchoolSelectVo> schoolVos = (List<SchoolSelectVo>)redisTemplate.opsForValue().get("school");
        if(schoolVos == null){
            List<SchoolSelectVo> schoolSelectVos = schoolService.getAllSchoolSelect();
            redisTemplate.opsForValue().set("school",schoolSelectVos,1, TimeUnit.HOURS);
            return ServerResponse.success(schoolSelectVos);
        }
        return ServerResponse.success(schoolVos);
    }
}

