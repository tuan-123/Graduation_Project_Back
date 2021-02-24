package com.zsc.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.util.EpidemicUtil;
import com.zsc.springboot.vo.SchoolSelectVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/22 10:10
 */

@RequestMapping("/epidemic")
@RestController
public class EpidemicMapController {

    @RequiresAuthentication
    @ApiOperation(value = "中国累计疫情人数",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getAllChina")
    public ServerResponse getAllChina() throws IOException {
        String allResult = EpidemicUtil.getAllData();
        //加工
        JSONObject resultObject = JSONObject.parseObject(allResult);
        JSONArray allChina = resultObject.getJSONArray("data");
        return ServerResponse.success(allChina);
    }

    @RequiresAuthentication
    @ApiOperation(value = "中国今日新增",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getTodayChina")
    public ServerResponse getTodayChina() throws IOException {
        String allResult = EpidemicUtil.getAllData();
        //加工
        JSONObject resultObject = JSONObject.parseObject(allResult);
        JSONArray today = resultObject.getJSONArray("today");
        return ServerResponse.success(today);
    }

    @RequiresAuthentication
    @ApiOperation(value = "全球累计",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getAllWorld")
    public ServerResponse getAllWorld() throws IOException {
        String allResult = EpidemicUtil.getAllData();
        //加工
        JSONObject resultObject = JSONObject.parseObject(allResult);
        JSONArray g_data = resultObject.getJSONArray("g_data");
        return ServerResponse.success(g_data);
    }

    @RequiresAuthentication
    @ApiOperation(value = "全球今日新增",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/getTodayWorld")
    public ServerResponse getTodayWorld() throws IOException {
        String allResult = EpidemicUtil.getAllData();
        //加工
        JSONObject resultObject = JSONObject.parseObject(allResult);
        JSONArray g_today = resultObject.getJSONArray("g_today");
        return ServerResponse.success(g_today);
    }

}
