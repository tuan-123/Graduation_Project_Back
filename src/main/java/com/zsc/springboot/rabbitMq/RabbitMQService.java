package com.zsc.springboot.rabbitMq;

import com.zsc.springboot.common.SendEmailCode;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/12 16:28
 */
@Service
public class RabbitMQService {

    private static String subject = "团哥测试验证码";
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    private static String fromUser;
    @Value("${spring.mail.username}")
    public void setFromUser(String from){
        fromUser = from;
    }


    @RabbitListener(bindings = @QueueBinding(value =
                                @Queue("fanout_queue_email"),exchange =
                                @Exchange(value = "fanout_exchange",type = "fanout")))
    public void psubConsumerEmailAno(SendEmailCode sendEmailCode) throws InterruptedException {

        String text = "您的本次注册验证码为：" + sendEmailCode.getCode() + ",有效时间为5分钟。\n祝您生活愉快！";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromUser);
        message.setTo(sendEmailCode.getEmail());
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
}
