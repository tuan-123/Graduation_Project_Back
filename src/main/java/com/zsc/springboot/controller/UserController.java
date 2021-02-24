package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.form.FindPasswordForm;
import com.zsc.springboot.form.LoginForm;
import com.zsc.springboot.form.RegisterForm;
import com.zsc.springboot.form.UpdatePsdForm;
import com.zsc.springboot.service.impl.SchoolServiceImpl;
import com.zsc.springboot.service.impl.UserServiceImpl;
import com.zsc.springboot.util.*;
import com.zsc.springboot.vo.UserDetailInfoVo;
import com.zsc.springboot.vo.UserIndexVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private String outerId = "HGT_OUTER_ID";  // 目前只用一个测试，后期应该由管理员手动添加和设置该outerId

    @Autowired
    private EmailCodeUtil emailCodeUtil;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SchoolServiceImpl schoolService;

    @ApiOperation(value = "发送验证码接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/sendEmailCode")
    public ServerResponse sendEmailCode(String phone,String email){
        boolean sendCode = emailCodeUtil.sendEmailCode(phone,email);
        return sendCode?ServerResponse.success(null):ServerResponse.fail("发送失败");
    }


    @ApiOperation(value = "注册接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/register")
    public ServerResponse register(@Validated @RequestBody RegisterForm registerForm){
        return userService.register(registerForm);
    }

    @ApiOperation(value = "登录接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/login")
    public ServerResponse login(@Validated @RequestBody LoginForm loginForm){
        return userService.login(loginForm);
    }

    @ApiOperation(value = "登录接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/faceLogin")
    public ServerResponse faceLogin(MultipartFile multipartFile) throws Exception {
        String faceToken = FacePPUtil.imgToFaceToken(multipartFile);
        if(faceToken != null){
            String userId = FacePPUtil.searchFaceByFaceToken(faceToken,outerId);
            if(userId == null || userId.equals("-1")){
                return ServerResponse.fail("人脸登录失败");
            }
            String password = userService.getPasswordByUserId(userId);
            return ServerResponse.loginSuccess(userService.getUserByUserId(userId), JWTUtil.sign(userId,password));
        }
        return ServerResponse.fail("人脸登录失败");
    }

    @RequiresAuthentication
    @GetMapping("/getNameAndHImg")
    @ApiOperation(value = "获取昵称和头像",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getNameAndHImg(@RequestParam(required = true,value = "userId") String userId){
        UserIndexVo userIndexVo = userService.getUserNameAndHImg(userId);
        if(userIndexVo != null){
            return ServerResponse.success(userIndexVo);
        }else{
            return ServerResponse.nullData();
        }

    }

    @RequiresAuthentication
    @GetMapping("/getUserDetailInfo")
    @ApiOperation(value = "获取用户信息",response = ServerResponse.class,httpMethod = "GET")
    public ServerResponse getUserDetailInfo(@RequestParam(required = true,value = "userId") String userId){
        UserDetailInfoVo userDetailInfoVo = userService.getUserDetailInfo(userId);
        if(userDetailInfoVo != null){
            return ServerResponse.success(userDetailInfoVo);
        }else{
            return ServerResponse.nullData();
        }
    }

    @RequiresAuthentication
    @PutMapping("/updateNickName")
    @ApiOperation(value = "修改昵称",response = ServerResponse.class,httpMethod = "PUT")
    public ServerResponse updateNickName(@RequestParam("userId")String userId,@RequestParam("nickName")String nickName){
        Integer updateResult = userService.updateNickName(userId,nickName);
        if(updateResult == 1){
            return ServerResponse.success(null);
        }else{
            return ServerResponse.fail("修改失败");
        }
    }

    @RequiresAuthentication
    @PutMapping("/updateSchoolNum")
    @ApiOperation(value = "修改学号",response = ServerResponse.class,httpMethod = "PUT")
    public ServerResponse updateSchoolNum(@RequestParam("userId") String userId,@RequestParam("schoolNum") String schoolNum){
        Integer updateResult = userService.updateSchoolNum(userId,schoolNum);
        if(updateResult == 1){
            return ServerResponse.success(null);
        }else{
            return ServerResponse.fail("绑定失败");
        }
    }

    @RequiresAuthentication
    @PutMapping("/updateSchool")
    @ApiOperation(value = "修改学校",response = ServerResponse.class,httpMethod = "PUT")
    public ServerResponse updateSchool(@RequestParam("userId") String userId,@RequestParam("schoolId") Integer schoolId){
        Integer updateResult = userService.updateSchool(userId,schoolId);
        if(updateResult == 1){
            return ServerResponse.success(null);
        }else{
            return ServerResponse.fail("绑定失败");
        }
    }

    @RequiresAuthentication
    @PostMapping("/updateImage")
    @ApiOperation(value = "修改头像",response = ServerResponse.class,httpMethod = "POST")
    public ServerResponse updateImage(HttpServletRequest request,MultipartFile file,String userId){
        //System.out.println("=================");
        //System.out.println(userId);
        //System.out.println(file);
        String imgUrl = UserImgHandlerUtil.handleImg(request,file,userId);
        //更新数据库
        Integer updateResult = userService.updateImg(userId,imgUrl);
        if(updateResult == 1){
            return ServerResponse.success(imgUrl);
        }
        return ServerResponse.fail("更新失败");
    }

    @RequiresAuthentication
    @ApiOperation(value = "修改密码",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/updatePassword")
    public ServerResponse updatePassword(@Validated @RequestBody UpdatePsdForm updatePsdForm){
        Integer updateResult = userService.updatePassword(updatePsdForm.getUserId(),updatePsdForm.getOldPassword(),updatePsdForm.getNewPassword());
        if(updateResult == -1){
            return ServerResponse.fail("原密码错误");
        }else if(updateResult == 0){
            return ServerResponse.fail("修改失败");
        }else {
            return ServerResponse.success(null);
        }
    }

    @ApiOperation(value = "退出登录",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping(value = "logout")
    public ServerResponse logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ServerResponse.success(null);
    }

    @RequiresAuthentication
    @ApiOperation(value = "找回密码验证邮箱",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/findPasswordSendCode")
    public ServerResponse findPasswordSendCode(@RequestParam("userId")String userId,@RequestParam("email")String email){
        //接收可以了，记得验证邮箱
        Integer checkResult = userService.checkByUserIdAndEmail(userId,email);
        if(checkResult == 0){
            return ServerResponse.fail("邮箱与账号不匹配");
        }
        boolean sendCode = emailCodeUtil.sendEmailCode(userId,email);
        return sendCode?ServerResponse.success(null):ServerResponse.fail("发送失败");
    }

    @ApiOperation(value = "找回密码接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/findPassword")
    public ServerResponse findPassword(@Validated @RequestBody FindPasswordForm findPasswordForm){
        Integer result = userService.findPassword(findPasswordForm);
        if(result == 1){
            return ServerResponse.success(null);
        }else if(result == -2){
            return ServerResponse.fail("验证码不正确");
        }else if(result == -1){
            return ServerResponse.fail("用户不存在");
        }else{
            return ServerResponse.fail("找回密码失败");
        }
    }

}

