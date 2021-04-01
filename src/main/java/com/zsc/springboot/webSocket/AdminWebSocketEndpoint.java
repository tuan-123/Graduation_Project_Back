package com.zsc.springboot.webSocket;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/17 10:52
 */

@ServerEndpoint(value = "/webSocket/admin/{userId}")
@Component
public class AdminWebSocketEndpoint {
    private Session session;
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        AdminSessionPool.sessions.put(userId,session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        AdminSessionPool.close(session.getId());  // sessionId 是系统自动生成的
        session.close();
        System.out.println("=========onclose");
    }

    @OnMessage
    public void onMessage(String message, Session session){
        //SessionPool.sendMessage(message);

        //如果是心跳检测消息，则返回pong作为心跳回应
        if(message.equalsIgnoreCase("ping")){
            try {
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("type","pong");
                session.getBasicRemote().sendText(JSON.toJSONString(params));
                System.out.println("应答客户端的消息" + JSON.toJSONString(params));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Map<String, Object> params = JSON.parseObject(message, new HashMap<String, Object>().getClass());
            AdminSessionPool.sendMessageToUserId(params);
        }
    }
}
