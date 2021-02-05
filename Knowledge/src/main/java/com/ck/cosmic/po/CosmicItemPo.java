package com.ck.cosmic.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "ck_cosmic_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CosmicItemPo {

    private Long id;

    @Column(name = "user_demand")
    private Long userDemand;

    @Column(name = "trigger_event", length = 256)
    private String triggerEvent;

    @Column(name = "func_process", length = 256)
    private String funcProcess;

    @Column(name = "sub_func_process", length = 256)
    private String subFuncProcess;

    @Column(name = "data_transfer", length = 1)
    private String dataTransfer;

    @Column(name = "data_group", length = 256)
    private String dataGroup;

    @Column(name = "data_prop", length = 256)
    private String dataProp;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "create_user")
    private Long createUser;

    @Column(name = "lastupd_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdDate;

    @Column(name = "lastupd_user")
    private Long lastUpdUser;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
}
