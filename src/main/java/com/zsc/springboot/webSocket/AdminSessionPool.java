package com.zsc.springboot.webSocket;

import com.alibaba.fastjson.JSON;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/17 16:08
 */
public class AdminSessionPool {
    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void close(String sessionId) throws IOException {
        for(String userSession : AdminSessionPool.sessions.keySet()){
            Session session = AdminSessionPool.sessions.get(userSession);
            if(session.getId().equals(sessionId)){
                sessions.remove(userSession);
                break;
            }
        }
    }

    public static void sendMessage(String sessionId,String message){
        sessions.get(sessionId).getAsyncRemote().sendText(message);
    }

    public static void sendMessage(String message){
        for(String sessionId : AdminSessionPool.sessions.keySet()){
            AdminSessionPool.sessions.get(sessionId).getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessageToUserId(Map<String,Object> params){
        String toUserId = params.get("toUserId").toString();
        String msg = params.get("message").toString();
        String fromUserId = params.get("fromUserId").toString();
        Session session = sessions.get(toUserId);
        if(session != null){
            session.getAsyncRemote().sendText("来自 " + fromUserId + " 的消息： " + msg);
        }
    }


    public static void sendMessage(AdminWebSocketMessageObject object){
        for(String sessionId : AdminSessionPool.sessions.keySet()){
            //System.out.println("==================有管理员在线");
            AdminSessionPool.sessions.get(sessionId).getAsyncRemote().sendText(JSON.toJSONString(object));
        }
    }

    public static void sendMessage(Map<String,Object> params){
        for(String sessionId : AdminSessionPool.sessions.keySet()){
            AdminSessionPool.sessions.get(sessionId).getAsyncRemote().sendText(JSON.toJSONString(params));
        }
    }
}
