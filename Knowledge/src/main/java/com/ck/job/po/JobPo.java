package com.ck.job.po;

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

@Entity(name = "ck_job")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPo {

    private Long id;

    @Column(length = 128)
    private String name;

    @Column(length = 128)
    private String label;

    @Column(length = 8)
    private String type;

    @Column(length = 16)
    private String cron;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "lastupd_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdDate;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

}
