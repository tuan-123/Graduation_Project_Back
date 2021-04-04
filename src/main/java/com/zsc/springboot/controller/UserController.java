package com.zsc.springboot.controller;


import com.zsc.springboot.common.SendEmailCode;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.form.FindPasswordForm;
import com.zsc.springboot.form.LoginForm;
import com.zsc.springboot.form.RegisterForm;
import com.zsc.springboot.form.UpdatePsdForm;
import com.zsc.springboot.form.admin.AddUserForm;
import com.zsc.springboot.service.UserService;
import com.zsc.springboot.util.*;
import com.zsc.springboot.vo.UserDetailInfoVo;
import com.zsc.springboot.vo.UserIndexVo;
import com.zsc.springboot.webSocket.WebSocketEndpoint;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    @ApiOperation(value = "发送验证码接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/sendEmailCode")
    public ServerResponse sendEmailCode(String phone,String email){
        boolean sendCode = emailCodeUtil.sendEmailCode(phone,email);
        return sendCode?ServerResponse.success(null):ServerResponse.fail("发送失败");
    }
    */

    @ApiOperation(value = "发送验证码接口",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/sendEmailCode")
    public ServerResponse sendEmailCode(String phone,String email){
        if(phone == null || email == null)
            return ServerResponse.fail("手机号或邮箱不能为空");
        //生成验证码  并存入缓存
        String code = emailCodeUtil.createEmailCode(phone);
        if(code == null)
            return ServerResponse.fail("获取失败");
        SendEmailCode sendEmailCode = new SendEmailCode();
        sendEmailCode.setEmail(email);
        sendEmailCode.setCode(code);
        rabbitTemplate.convertAndSend("fanout_exchange","",sendEmailCode);
        return ServerResponse.success(null);
    }

    @ApiOperation(value = "关闭人脸登录接口",response = ServerResponse.class,httpMethod = "GET")
    @RequiresAuthentication
    @GetMapping("/closeFaceLogin")
    public ServerResponse closeFaceLogin(@RequestParam("userId") String userId) throws Exception {
        String faceToken = userService.getFaceTokenByUserId(userId);
        boolean remove = FacePPUtil.remoteFaceFromFaceSet(AESUtil.encrypt(faceToken,userId),outerId);
        if(!remove)
            return ServerResponse.fail("关闭失败");
        Integer update = userService.removeFaceLogin(userId);
        if(update == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("关闭失败");
    }

    @ApiOperation(value = "开启人脸登录接口",response = ServerResponse.class,httpMethod = "POST")
    @RequiresAuthentication
    @PostMapping("/addFaceLogin")
    public ServerResponse addFaceLogin(MultipartFile multipartFile,String userId) throws Exception {
        if(userId == null || userId.length() <= 0)
            return ServerResponse.fail("用户Id不能为空");
        //System.out.println(FacePPUtil.imgToFaceToken(ImgCompressUtil.compressPicForScale(multipartFile.getBytes(),500)));

        String faceToken = FacePPUtil.imgToFaceToken(ImgCompressUtil.compressPicForScale(multipartFile.getBytes(),500));
        if(faceToken.equals("-1"))
            return ServerResponse.fail("识别到多个人脸");
        boolean addFaceTokenToFaceSet = FacePPUtil.addFaceTokenToFaceSet(faceToken, outerId);
        if(!addFaceTokenToFaceSet)
            return ServerResponse.fail("Add FaceSet Fail");
        boolean addUserIDToFaceToken = FacePPUtil.addUserIDToFaceToken(userId, faceToken);
        if(!addUserIDToFaceToken)
            return ServerResponse.fail("Add USERID Fail");
        Integer update = userService.addFaceLogin(userId,faceToken);
        if(update != 1) {
            FacePPUtil.remoteFaceFromFaceSet(AESUtil.decrypt(faceToken, userId), outerId);
            return ServerResponse.fail("失败");
        }

        return ServerResponse.success(null);
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
        String faceToken = FacePPUtil.imgToFaceToken(ImgCompressUtil.compressPicForScale(multipartFile.getBytes(),500));
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
    @GetMapping(value = "/logout")
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
        /*
        boolean sendCode = emailCodeUtil.sendEmailCode(userId,email);
        return sendCode?ServerResponse.success(null):ServerResponse.fail("发送失败");
         */
        String code = emailCodeUtil.createEmailCode(userId);
        if(code == null)
            return ServerResponse.fail("获取失败");
        SendEmailCode sendEmailCode = new SendEmailCode();
        sendEmailCode.setEmail(email);
        sendEmailCode.setCode(code);
        rabbitTemplate.convertAndSend("fanout_exchange","",sendEmailCode);
        return ServerResponse.success(null);
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

    @ApiOperation(value = "管理员登录",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/login")
    public ServerResponse adminLogin(@Validated @RequestBody LoginForm loginForm){
        return userService.adminLogin(loginForm.getPhone(),loginForm.getPassword());
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "获取用户列表",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/getUserList")
    public ServerResponse adminGetUserList(@RequestParam("query")String query,@RequestParam("pageNum")long pageNum,@RequestParam("pageSize")long pageSize){
        return ServerResponse.success(userService.getUserList(query,pageNum,pageSize));
    }

    @OperLogAnnotation(operType = "修改",operModul = "用户管理",operDesc = "修改用户状态")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "修改用户状态",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/changeStateById")
    public ServerResponse adminChangeStateById(@RequestParam("userId")String userId,@RequestParam("state")Integer state){
        Integer updateResult = userService.changeStateById(userId,state);
        if(updateResult == 0)
            return ServerResponse.fail("修改失败");
        return ServerResponse.success(null);
    }

    @OperLogAnnotation(operType = "删除",operModul = "用户管理",operDesc = "删除用户")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "删除用户",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/deleteUserById")
    public ServerResponse adminDeleteUserById(@RequestParam("userId")String userId){
        String faceToken = userService.getFaceTokenByUserId(userId);
        if(faceToken == null || ("").equals(faceToken)){
            try {
                FacePPUtil.remoteFaceFromFaceSet(AESUtil.decrypt(faceToken,userId),outerId);
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.fail("删除失败");
            }
            userService.removeFaceLogin(userId);
        }
        Integer deleteUserById = userService.deleteUserById(userId);
        if(deleteUserById == 0)
            return ServerResponse.fail("删除失败");
        return ServerResponse.success(null);
    }

    @OperLogAnnotation(operModul = "用户管理",operType = "添加",operDesc = "添加用户")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "添加用户",response = ServerResponse.class,httpMethod = "POST")
    @PostMapping("/admin/addUser")
    public ServerResponse adminAddUser(@Validated@RequestBody AddUserForm addUserForm){
        Integer addUser = userService.addUser(addUserForm.getUserId(),addUserForm.getPassword(),addUserForm.getEmail(),addUserForm.getRole());
        if(addUser == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("添加失败");
    }

    @OperLogAnnotation(operModul = "用户管理",operType = "修改",operDesc = "解绑用户学校")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "解绑学校",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/unBundlingSchool")
    public ServerResponse unBundlingSchool(@RequestParam("userId")String userId){
        Integer unBundlingSchoolById = userService.unBundlingSchoolById(userId);
        if(unBundlingSchoolById == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("解绑失败");
    }

    @OperLogAnnotation(operModul = "用户管理",operType = "修改",operDesc = "解绑用户学号")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "解绑学号",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/unBundlingSchoolNum")
    public ServerResponse unBundlingSchoolNum(@RequestParam("userId")String userId){
        Integer unBundlingSchoolNumById = userService.unBundlingSchoolNumById(userId);
        if(unBundlingSchoolNumById == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("解绑失败");
    }

    @OperLogAnnotation(operModul = "用户管理",operType = "修改",operDesc = "修改用户所绑定邮箱")
    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "修改邮箱",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/updateEmail")
    public ServerResponse updateEmail(@RequestParam("userId")String userId,@RequestParam("email") @Email String email){
        Integer updateEmail = userService.updateEmail(userId,email);
        if(updateEmail == 1)
            return ServerResponse.success(null);
        return ServerResponse.fail("修改失败");
    }

    @RequiresAuthentication
    @RequiresRoles({"2"})
    @ApiOperation(value = "获取主页数据",response = ServerResponse.class,httpMethod = "GET")
    @GetMapping("/admin/getIndexData")
    public ServerResponse getIndexData(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userOnline", WebSocketEndpoint.getOnlineCount());
        hashMap.put("askCount",WebSocketEndpoint.getAskCount());
        hashMap.put("helpCount",WebSocketEndpoint.getHelpCount());
        hashMap.put("idleCount",WebSocketEndpoint.getIdleCount());
        return ServerResponse.success(hashMap);
    }



}

