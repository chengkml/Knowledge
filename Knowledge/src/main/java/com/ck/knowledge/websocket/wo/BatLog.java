package com.ck.knowledge.websocket.wo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatLog extends SocketData{

    public BatLog(String log){
        this.log = log;
    }

    private String log;

    @Override
    public String getTopic() {
        return "batLog";
    }
}
