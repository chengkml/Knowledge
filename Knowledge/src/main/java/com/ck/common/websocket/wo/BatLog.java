package com.ck.common.websocket.wo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatLog extends SocketData{

    public BatLog(String processId, String log, int runningProcessNum){
        this.processId = processId;
        this.log = log;
        this.runningProcessNum = runningProcessNum;
    }

    private String processId;

    private String log;

    private int runningProcessNum;

    @Override
    public String getTopic() {
        return "batLog";
    }
}
