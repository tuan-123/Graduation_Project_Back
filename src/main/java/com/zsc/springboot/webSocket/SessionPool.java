package com.zsc.springboot.webSocket;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionPool {
    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void close(String sessionId) throws IOException{
        for(String userSession : SessionPool.sessions.keySet()){
            Session session = SessionPool.sessions.get(userSession);
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
        for(String sessionId : SessionPool.sessions.keySet()){
            SessionPool.sessions.get(sessionId).getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessage(Map<String,Object> params){
        String toUserId = params.get("toUserId").toString();
        String msg = params.get("message").toString();
        String fromUserId = params.get("fromUserId").toString();
        Session session = sessions.get(toUserId);
        if(session != null){
            session.getAsyncRemote().sendText("来自 " + fromUserId + " 的消息： " + msg);
        }
    }
}
