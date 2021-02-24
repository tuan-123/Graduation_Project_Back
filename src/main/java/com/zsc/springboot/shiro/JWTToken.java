package com.zsc.springboot.shiro;
/**
 *
 *  JWTToken
 *@Author：黄港团
 *@Since：2021/1/12 21:28
 */

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 把Shiro默认的Token改为JWT
 */
public class JWTToken implements AuthenticationToken {
    //以下相当于重写shiro框架自带的token
    private String token;
    public JWTToken(String token){
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
