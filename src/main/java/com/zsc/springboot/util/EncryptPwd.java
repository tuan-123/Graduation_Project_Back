package com.zsc.springboot.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 *  EncryptPwd 密码加密器
 *@Author：黄港团
 *@Since：2021/1/12 16:57
 */
@Component
public class EncryptPwd {
    /*
    //@Value 不能直接给静态变量赋值
    @Value("${zsc.jwt.hashIterations}")
    private static int hashIterations;

     */

    public static int hashIterations;
    // 交给spring去注入,记得加上@Component
    @Value("${zsc.jwt.hashIterations}")
    public void setHashIterations(int hashIterations){
        this.hashIterations = hashIterations;
    }
    public static String encrypt(String password,String salt){
        return new Md5Hash(password,salt,hashIterations).toString();
    }
}
