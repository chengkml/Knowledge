package com.ck.bat.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RunningProcess {
    public RunningProcess(String name, String label, Process process) {
        this.name = name;
        this.label = label;
        this.process = process;
        this.startTime = new Date();
    }

    private String name;

    private String label;

    private Process process;

    private Date startTime;

}
