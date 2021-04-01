package com.zsc.springboot.handler;

import com.alibaba.fastjson.JSON;
import com.zsc.springboot.config.annotation.OperLogAnnotation;
import com.zsc.springboot.entity.ExceptionLog;
import com.zsc.springboot.entity.OperLog;
import com.zsc.springboot.service.ExceptionLogService;
import com.zsc.springboot.service.OperLogService;
import com.zsc.springboot.util.IPUtil;
import com.zsc.springboot.util.JWTUtil;
import com.zsc.springboot.util.QQWry;
import com.zsc.springboot.util.SnowflakeIdWorker;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/30 19:52
 */
@Aspect
@Component
public class OperLogAspect {

    //@Autowired
    //private QQWry qqWry;

    @Autowired
    private OperLogService operLogService;
    @Autowired
    private ExceptionLogService exceptionLogService;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.zsc.springboot.config.annotation.OperLogAnnotation)")
    public void operLogPointCut(){}

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.zsc.springboot.controller..*.*(..))")
    public void operExceptionLogPoinCut() {}

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPointCut()",returning = "keys")
    public void saveOperLog(JoinPoint joinPoint,Object keys){
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        OperLog operLog = new OperLog();
        try{
            operLog.setOperId(SnowflakeIdWorker.generateId());

            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLogAnnotation operLogAnnotation = method.getAnnotation(OperLogAnnotation.class);
            if(operLogAnnotation != null){
                String operModul = operLogAnnotation.operModul();
                String operType = operLogAnnotation.operType();
                String operDesc = operLogAnnotation.operDesc();
                operLog.setOperModul(operModul);
                operLog.setOperType(operType);
                operLog.setOperDesc(operDesc);
            }
            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            operLog.setOperMethod(methodName);

            //请求的参数
            Map<String,String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);

            operLog.setOperRequParam(params); // 请求参数
            operLog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果

            Object o = SecurityUtils.getSubject().getPrincipal();
            String token = null;
            if(o != null){
                token = (String) o;
            }
            String userId = JWTUtil.getUserId(token);
            operLog.setOperUserId(userId); // 请求用户ID
            String ip = IPUtil.getIpAddress(request);
            // 是否为局域网 为局域网时另外获取外网ip
            if("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)){
                ip = IPUtil.getOutIPV4();
            }
            // 这里可以根据ip获取所在地，用的是纯真ip库，可能会有点不准
            //String adress = qqWry.findIP(ip).toString();

            operLog.setOperIp(ip); // 请求IP
            operLog.setOperUri(request.getRequestURI()); // 请求URI
        }catch (Exception e){
            e.printStackTrace();;
        }


        operLogService.inserOperLog(operLog);

    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        ExceptionLog exceptionLog = new ExceptionLog();

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();

            exceptionLog.setExcId(SnowflakeIdWorker.generateId());

            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);

            exceptionLog.setExcRequParam(params);
            exceptionLog.setOperMethod(methodName);
            exceptionLog.setExcName(e.getClass().getName());
            exceptionLog.setExcMessage(stackTraceToString(e.getClass().getName(),e.getMessage(),e.getStackTrace()));

            Object o = SecurityUtils.getSubject().getPrincipal();
            String token = null;
            if(o != null){
                token = (String) o;
            }
            String userId = JWTUtil.getUserId(token);

            exceptionLog.setOperUserId(userId);

            exceptionLog.setOperUri(request.getRequestURI());

            String ip = IPUtil.getIpAddress(request);
            if("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip))
                ip = IPUtil.getOutIPV4();

            exceptionLog.setOperIp(ip);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

        exceptionLogService.insertExceptionLog(exceptionLog);

    }



    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
