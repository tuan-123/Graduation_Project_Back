package com.zsc.springboot.component;

import com.zsc.springboot.service.AskService;
import com.zsc.springboot.service.HelpService;
import com.zsc.springboot.service.IdleService;
import com.zsc.springboot.webSocket.WebSocketEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/24 15:40
 */
@Component
public class MyRunner implements ApplicationRunner {
    @Autowired
    private IdleService idleService;
    @Autowired
    private HelpService helpService;
    @Autowired
    private AskService askService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        WebSocketEndpoint.setIdleCount(idleService.getCount());
        WebSocketEndpoint.setHelpCount(helpService.getCount());
        WebSocketEndpoint.setAskCount(askService.getCount());
    }
}
