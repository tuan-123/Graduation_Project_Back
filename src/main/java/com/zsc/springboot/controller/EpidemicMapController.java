package com.zsc.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.util.EpidemicUtil;
import com.zsc.springboot.vo.SchoolSelectVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/22 10:10
 */

@RequestMapping("/epidemic")
@RestController
public class EpidemicMapController {

    @Autowired
    private RedisTemplate redisTemplate;

    //@RequiresAuthentication
    @ApiOperation(value = "中国累计疫情人数",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getAllChina")
    public ServerResponse getAllChina() throws IOException {
        JSONObject allEpidemic = (JSONObject)redisTemplate.opsForValue().get("epidemic");
        if(allEpidemic == null){
            String allResult = EpidemicUtil.getAllData();
            //加工
            JSONObject resultObject = JSONObject.parseObject(allResult);
            redisTemplate.opsForValue().set("epidemic",resultObject,1, TimeUnit.HOURS);
            JSONArray allChina = resultObject.getJSONArray("data");
            return ServerResponse.success(allChina);
        }
        JSONArray allChina = allEpidemic.getJSONArray("data");
        return ServerResponse.success(allChina);
    }

    @RequiresAuthentication
    @ApiOperation(value = "中国今日新增",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getTodayChina")
    public ServerResponse getTodayChina() throws IOException {
        JSONObject allEpidemic = (JSONObject) redisTemplate.opsForValue().get("epidemic");
        if(allEpidemic == null){
            String allResult = EpidemicUtil.getAllData();
            //加工
            JSONObject resultObject = JSONObject.parseObject(allResult);
            redisTemplate.opsForValue().set("epidemic",resultObject,1, TimeUnit.HOURS);
            JSONArray today = resultObject.getJSONArray("today");
            return ServerResponse.success(today);
        }
        JSONArray today = allEpidemic.getJSONArray("today");
        return ServerResponse.success(today);
    }

    @RequiresAuthentication
    @ApiOperation(value = "全球累计",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getAllWorld")
    public ServerResponse getAllWorld() throws IOException {
        JSONObject allEpidemic = (JSONObject) redisTemplate.opsForValue().get("epidemic");
        if(allEpidemic == null){
            String allResult = EpidemicUtil.getAllData();
            //加工
            JSONObject resultObject = JSONObject.parseObject(allResult);
            redisTemplate.opsForValue().set("epidemic",resultObject,1, TimeUnit.HOURS);
            JSONArray g_data = resultObject.getJSONArray("g_data");
            return ServerResponse.success(g_data);
        }
        JSONArray g_data = allEpidemic.getJSONArray("g_data");
        return ServerResponse.success(g_data);
    }

    @RequiresAuthentication
    @ApiOperation(value = "全球今日新增",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getTodayWorld")
    public ServerResponse getTodayWorld() throws IOException {
        JSONObject allEpidemic = (JSONObject) redisTemplate.opsForValue().get("epidemic");
        if(allEpidemic == null){
            String allResult = EpidemicUtil.getAllData();
            //加工
            JSONObject resultObject = JSONObject.parseObject(allResult);
            redisTemplate.opsForValue().set("epidemic",resultObject,1, TimeUnit.HOURS);
            JSONArray g_today = resultObject.getJSONArray("g_today");
            return ServerResponse.success(g_today);
        }
        JSONArray g_today = allEpidemic.getJSONArray("g_today");
        return ServerResponse.success(g_today);
    }

}
