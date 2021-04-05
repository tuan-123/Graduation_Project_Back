package com.zsc.springboot.schedule;

import com.zsc.springboot.webSocket.AdminSessionPool;
import com.zsc.springboot.webSocket.AdminWebSocketMessageObject;
import com.zsc.springboot.webSocket.SessionPool;
import com.zsc.springboot.webSocket.WebSocketEndpoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/4/3 16:10
 */

@Component
@EnableScheduling
@EnableAsync
public class AdminSchedule {

    private HashMap<String,Object> hashMap = new HashMap<>();
    @Scheduled(fixedDelay = 10000)
    @Async
    public void adminPushDataToWeb(){
        //System.out.println(Thread.currentThread().getName());
        if(!AdminSessionPool.sessions.isEmpty()){
            hashMap.put("userOnline",WebSocketEndpoint.getOnlineCount());
            hashMap.put("askCount",WebSocketEndpoint.getAskCount());
            hashMap.put("helpCount",WebSocketEndpoint.getHelpCount());
            hashMap.put("idleCount",WebSocketEndpoint.getIdleCount());
            AdminSessionPool.sendMessage(hashMap);
        }
    }
}
