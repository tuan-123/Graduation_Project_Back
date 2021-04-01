package com.zsc.springboot.rabbitMq;

import com.rabbitmq.client.Channel;
import com.zsc.springboot.common.SendEmailCode;
import com.zsc.springboot.webSocket.WebSocketEndpoint;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/12 16:28
 */
@Service
public class RabbitMQService {

    private static String subject = "毕设";
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    private static String fromUser;
    @Value("${spring.mail.username}")
    public void setFromUser(String from){
        this.fromUser = from;
    }


    /*@RabbitListener(bindings = @QueueBinding(value =
                                @Queue("fanout_queue_email"),exchange =
                                @Exchange(value = "fanout_exchange",type = "fanout")))*/
    @RabbitListener(queues = "fanout_queue_email")
    public void psubConsumerEmailAno(SendEmailCode sendEmailCode, Message message, Channel channel) throws IOException {
       try {
            //String text = "您的本次注册验证码为：" + sendEmailCode.getCode() + ",有效时间为5分钟。\n祝您生活愉快！";
            String text = sendEmailCode.getCode();
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromUser);
            msg.setTo(sendEmailCode.getEmail());
            msg.setSubject(subject);
            msg.setText(text);
            javaMailSender.send(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
        /*System.out.println(fromUser);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);*/
        /*System.out.println(sendEmailCode.getEmail());
        System.out.println(sendEmailCode.getCode());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);*/

    }

    @RabbitListener(queues = "direct_queue_orders_ask")
    public void psubConsumerAskCountAno(Message message, Channel channel) throws IOException {
        try {
            WebSocketEndpoint.addAskCount();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    @RabbitListener(queues = "direct_queue_orders_help")
    public void psubConsumerHelpCountAno(Message message, Channel channel) throws IOException {
        try {
            WebSocketEndpoint.addHelpCount();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    @RabbitListener(queues = "direct_queue_orders_idle")
    public void psubConsumerIdleCountAno(Message message, Channel channel) throws IOException {
        try {
            WebSocketEndpoint.addIdleCount();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }
}
