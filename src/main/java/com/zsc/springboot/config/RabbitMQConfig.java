package com.zsc.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/12 16:01
 */
@Configuration
public class RabbitMQConfig {
    // 自定义消息转换器
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    // 1.定义fanout类型的交换器
    @Bean
    public Exchange fanout_exchange(){
        return ExchangeBuilder.fanoutExchange("fanout_exchange").build();
    }

    // 2.定义队列   durable:true 表示要持久化
    @Bean
    public Queue fanout_queue_email(){
        return new Queue("fanout_queue_email",true);
    }

    // 3.将消息队列与交换器绑定
    @Bean
    public Binding bindingEmail(){
        return BindingBuilder.bind(fanout_queue_email()).to(fanout_exchange()).with("").noargs();
    }

    //使用convertAndSend方式发送消息，消息默认就是持久化的

    //======================
    @Bean
    public Exchange direct_exchange(){
        return ExchangeBuilder.directExchange("direct_exchange_orders").build();
    }
    @Bean
    public Queue direct_queue_askCount(){
        return new Queue("direct_queue_orders_ask",true);
    }
    @Bean
    public Binding bindingAskCount(){
        return BindingBuilder.bind(direct_queue_askCount()).to(direct_exchange()).with("askCount").noargs();
    }

    @Bean
    public Queue direct_queue_helpCount(){
        return new Queue("direct_queue_orders_help",true);
    }
    @Bean
    public Binding bindingHelpCount(){
        return BindingBuilder.bind(direct_queue_helpCount()).to(direct_exchange()).with("helpCount").noargs();
    }

    @Bean
    public Queue direct_queue_idleCount(){
        return new Queue("direct_queue_orders_idle",true);
    }
    @Bean
    public Binding bindingIdleCount(){
        return BindingBuilder.bind(direct_queue_idleCount()).to(direct_exchange()).with("idleCount").noargs();
    }
}
