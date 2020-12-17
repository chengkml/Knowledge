package com.ck.common.websocket;

import com.alibaba.fastjson.JSON;
import com.ck.common.websocket.wo.SocketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CkWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(CkWebSocketHandler.class);

    private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("connected");
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logger.info("connected out");
        sessions.remove(session);
    }

    public static void sendMsgToAll(SocketData data) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(JSON.toJSONString(data)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
