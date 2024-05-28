package com.debxrshi.collabcode.handler;

import com.debxrshi.collabcode.model.Room;
import com.debxrshi.collabcode.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class WebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private RoomRepository roomRepository;
    private static final Map<String, Set<WebSocketSession>> roomSessions = new HashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String roomId = getRoomId(session);
            Room room = roomRepository.findByUuid(roomId);
            if (room != null) {
                roomSessions.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
            } else {
                session.close(CloseStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        try {
            String roomId = getRoomId(session);
            if (roomId != null) {
                Set<WebSocketSession> sessions = roomSessions.get(roomId);
                if (sessions != null) {
                    for (WebSocketSession webSocketSession : sessions) {
                        if (webSocketSession.isOpen()) {
                            webSocketSession.sendMessage(textMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to send message");
            e.printStackTrace();
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
        return parts.length > 1 ? parts[parts.length - 1] : null;
    }
}