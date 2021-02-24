package com.zsc.springboot.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常
 *
 *@Author：黄港团
 *@Since：2021/1/12 16:47
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理Assert的异常
     */
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ServerResponse handler(IllegalArgumentException e) throws IOException {
        log.error("Assert异常:-------------->{}",e.getMessage());
        return ServerResponse.fail(e.getMessage());
    }
    /**
     * @Validated
    校验错误异常处理
     */
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ServerResponse handler(MethodArgumentNotValidException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return ServerResponse.fail(objectError.getDefaultMessage());
    }

    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public ServerResponse handler(RuntimeException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        return ServerResponse.fail(e.getMessage());
    }

    //新增AuthenticationException异常捕获   hgt   2021.01.18
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnknownAccountException.class)
    public ServerResponse handler(UnknownAccountException e){
        log.error("运行时异常:-------------->",e);
        return ServerResponse.fail(e.getMessage());
    }

}
