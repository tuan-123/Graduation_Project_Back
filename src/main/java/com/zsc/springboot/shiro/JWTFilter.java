package com.zsc.springboot.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 *  JWTFilter
 *@Author：黄港团
 *@Since：2021/1/12 21:44
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
    //@Value("${zsc.jwt.header}")
    private String header = "Authorization";

    /**
     * 判断用户是否登录
     * 检测请求头是否包含token
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(header);
        return token != null;
    }
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(header);
        JWTToken jwtToken = new JWTToken(token);
        getSubject(request, response).login(jwtToken);
        return true;
    }
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)){
            try{
                executeLogin(request, response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
