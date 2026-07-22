package com.starlink.campus.config;

import org.springframework.stereotype.Component;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/api/ws/board/{roomNumber}")
@Component
public class WebSocketServer {

    private static final ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> lastActiveTimeMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomNumber") String roomNumber) {
        sessionMap.put(roomNumber, session);
        lastActiveTimeMap.put(roomNumber, System.currentTimeMillis());
        System.out.println("[WebSocket Server] 智慧班牌已连接, 教室编号: " + roomNumber);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomNumber") String roomNumber) {
        if (message.contains("\"type\":\"PING\"")) {
            lastActiveTimeMap.put(roomNumber, System.currentTimeMillis());
            try {
                session.getBasicRemote().sendText("{\"type\":\"PONG\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("roomNumber") String roomNumber) {
        sessionMap.remove(roomNumber);
        lastActiveTimeMap.remove(roomNumber);
        System.out.println("[WebSocket Server] 智慧班牌连接断开, 教室编号: " + roomNumber);
    }

    public static void checkZombieConnections() {
        long now = System.currentTimeMillis();
        lastActiveTimeMap.forEach((roomNumber, lastTime) -> {
            if (now - lastTime > 60000) { // 超过60秒未活跃
                Session session = sessionMap.get(roomNumber);
                if (session != null) {
                    try {
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sessionMap.remove(roomNumber);
                lastActiveTimeMap.remove(roomNumber);
                System.out.println("[WebSocket Server] 剔除僵尸连接, 教室编号: " + roomNumber);
            }
        });
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向指定班牌切换模式
     */
    public void broadcastToRoom(String roomNumber, String mode) {
        String msg = "{\"type\":\"MODE_SWITCH\",\"mode\":\"" + mode + "\"}";
        sendToBoard(roomNumber, msg);
    }

    /**
     * 向指定班牌发送广播消息
     */
    public static void sendToBoard(String roomNumber, String message) {
        Session session = sessionMap.get(roomNumber);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 全网广播消息到所有在线班牌
     */
    public static void broadcast(String message) {
        sessionMap.forEach((roomNumber, session) -> {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
