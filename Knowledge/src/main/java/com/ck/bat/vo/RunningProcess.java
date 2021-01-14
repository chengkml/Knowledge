package com.ck.bat.vo;

import com.ck.common.helper.IdHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RunningProcess {
    public RunningProcess(String name, String label, Process process) {
        this.id = IdHelper.getProcessId();
        this.name = name;
        this.label = label;
        this.process = process;
        this.startTime = new Date();
    }

    private String id;

    private String name;

    private String label;

    private Process process;

    private Date startTime;

}
