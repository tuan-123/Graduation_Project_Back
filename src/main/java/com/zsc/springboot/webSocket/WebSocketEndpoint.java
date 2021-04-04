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
 *@Since：2021/3/31 16:07
 */

// ws://localhost:8087/webSocket/用户id(参数)
@ServerEndpoint(value = "/webSocket/{userId}") // 对外暴露
@Component
public class WebSocketEndpoint {

    private Session session;

    private static long onlineCount = 0;

    private static long askCount = 0;
    private static long helpCount = 0;
    private static long idleCount = 0;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        SessionPool.sessions.put(userId,session);
        addOnlineCount();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        SessionPool.close(session.getId());  // sessionId 是系统自动生成的
        session.close();
        subOnlineCount();
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
            SessionPool.sendMessage(params);
        }
    }

    public static synchronized long getOnlineCount(){
        return onlineCount;
    }
    private static synchronized void addOnlineCount(){
        WebSocketEndpoint.onlineCount++ ;
    }
    private static synchronized void subOnlineCount(){
        WebSocketEndpoint.onlineCount--;
    }

    public static synchronized void setAskCount(long askCount){ WebSocketEndpoint.askCount = askCount;}
    public static synchronized void setHelpCount(long helpCount){ WebSocketEndpoint.helpCount = helpCount;}
    public static synchronized void setIdleCount(long idleCount){ WebSocketEndpoint.idleCount = idleCount;}
    public static synchronized long getAskCount(){ return askCount; }
    public static synchronized long getHelpCount(){ return helpCount; }
    public static synchronized long getIdleCount(){ return idleCount; }
    public static synchronized void addAskCount(){ WebSocketEndpoint.askCount++;}
    public static synchronized void subAskCount(){ WebSocketEndpoint.askCount--;}
    public static synchronized void addHelpCount(){ WebSocketEndpoint.helpCount++;}
    public static synchronized void subHelpCount(){ WebSocketEndpoint.helpCount--;}
    public static synchronized void addIdleCount(){ WebSocketEndpoint.idleCount++;}
    public static synchronized void subIdleCount(){ WebSocketEndpoint.idleCount--;}

}
