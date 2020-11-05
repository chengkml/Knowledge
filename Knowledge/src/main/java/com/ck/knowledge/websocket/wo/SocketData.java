package com.ck.knowledge.websocket.wo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SocketData {

    private String topic;

    public abstract String getTopic();

}
