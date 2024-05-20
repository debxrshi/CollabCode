package com.debxrshi.collabcode.handler;


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class WebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, Set<WebSocketSession>> roomSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getRoomId(session);
        if (roomId != null) {
            roomSessions.forEach((key, value) -> System.out.println("Key = " + key + ", Value = " + value));
            roomSessions.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        String roomId = getRoomId(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                for (WebSocketSession webSocketSession : sessions) {
                    if (webSocketSession.isOpen()) {
                        try {
                            webSocketSession.sendMessage(textMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = getRoomId(session);
        if (roomId != null) {
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    roomSessions.remove(roomId);
                }
            }
        }
    }

    private String getRoomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }
}