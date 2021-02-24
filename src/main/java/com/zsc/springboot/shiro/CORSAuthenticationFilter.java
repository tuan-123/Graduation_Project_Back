package com.zsc.springboot.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *CORSAuthenticationFilter
 *
 *@Author：黄港团
 *@Since：2021/1/12 22:09
 */
public class CORSAuthenticationFilter extends FormAuthenticationFilter {
    //跨域时会首先发送一个option请求，这里给请求设置正常状态,
    // 只能通过服务端对options请求做出正确的回应，这样才能保证options请求之后，post、put等请求可以被发出
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
