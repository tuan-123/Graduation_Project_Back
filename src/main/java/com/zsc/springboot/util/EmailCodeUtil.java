package com.zsc.springboot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *  使用该工具类请用自动注入方式，不能用new方式
 * 验证码生成工具
 *@Author：黄港团
 *@Since：2021/1/12 17:13
 */
@Slf4j
@Component
public class EmailCodeUtil {

    private static String subject = "团哥测试验证码";

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private RedisTemplate redisTemplate;

    private static String fromUser;
    @Value("${spring.mail.username}")
    public void setFromUser(String from){
        fromUser = from;
    }



    private static String getCode(){
        String str = "1234567890";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int)(Math.random() * str.length());
            code.append(str.charAt(index));
        }
        return code.toString();
    }

    //生成手机号与其对应的验证码 返回所生成的验证码
    public String createCodeForPhone(String phone){
        String code = getCode();
        redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
        return code;
    }

    public boolean sendEmailCode(String phone,String to){
        String code = getCode();
        String text = "您的本次注册验证码为：" + code + ",有效时间为5分钟。\n祝您生活愉快！";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromUser);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        //System.out.println(fromUser);
        //System.out.println(to);
        //System.out.println(subject);
        //System.out.println(text);
        javaMailSender.send(message);
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        log.info("发送邮件成功：" + to);
        return true;
    }

    public boolean checkEmailCode(String phone,String code){
        Object o = redisTemplate.opsForValue().get(phone);
        if(o == null)
            return false;
        String c = o.toString();
        if(!c.equals(code))
            return false;
        redisTemplate.delete(phone);
        return true;
    }

    public String createEmailCode(String phone){
        String code = getCode();
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        //log.info("发送邮件成功：" + to);
        return code;
    }



}
