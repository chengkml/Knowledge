package com.ck.knowledge.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class CkWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(CkWebSocketHandler.class);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("connected");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("connected out");
    }

}
