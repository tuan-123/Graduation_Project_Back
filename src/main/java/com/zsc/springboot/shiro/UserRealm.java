package com.zsc.springboot.shiro;

import com.zsc.springboot.service.impl.UserServiceImpl;
import com.zsc.springboot.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 *
 *  UserRealm
 *@Author：黄港团
 *@Since：2021/1/12 21:24
 */

/**
 * 定义shiro框架：用户域
 */
public class UserRealm extends AuthorizingRealm {

    /* 修改授权方法和认证方法的数据库查询

    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //拿到安全信息
        String phone = JWTUtil.getUserId(principalCollection.toString());
        User user = userMapper.selectById(phone);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> role = new HashSet<>();
        //记录当前登录用户的角色：0学生，1教师，2其他，3学校管理员，4超级管理员
        role.add(user.getRole().toString());
        authorizationInfo.setRoles(role);
        return authorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取token
        String token = (String) authenticationToken.getCredentials();
        //解密，获取userId(phone)
        String userId = JWTUtil.getUserId(token);
        String password = JWTUtil.getUserPassword(token);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",userId).eq("password",password);
        User user = userMapper.selectOne(queryWrapper);
        try{
            if(user != null && JWTUtil.verify(token)){
                return new SimpleAuthenticationInfo(token,token,this.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
     */

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //拿到安全信息
        String phone = JWTUtil.getUserId(principalCollection.toString());
        Integer roleFlag = userService.getRoleById(phone);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> role = new HashSet<>();
        //记录当前登录用户的角色：0学生，1教师，2其他，3学校管理员，4超级管理员
        role.add(roleFlag.toString());
        authorizationInfo.setRoles(role);
        return authorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取token
        String token = (String) authenticationToken.getCredentials();
        //解密，获取userId(phone)
        String userId = JWTUtil.getUserId(token);
        String password = JWTUtil.getUserPassword(token);
        //QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("phone", userId).eq("password", password);
        //User user = userMapper.selectOne(queryWrapper);
        Integer count = userService.checkLogin(userId,password);

        try {
            if (count == 1 && JWTUtil.verify(token)) {
                return new SimpleAuthenticationInfo(token, token, this.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
