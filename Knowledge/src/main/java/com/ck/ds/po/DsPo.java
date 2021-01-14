package com.ck.ds.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "ck_ds")
@Getter
@Setter
public class DsPo {

    private Long id;

    @Column(length = 32)
    private String name;

    @Column(length = 32)
    private String label;

    @Column(length = 32)
    private String type;

    @Column(length = 32)
    private String user;

    @Column(length = 32)
    private String pwd;

    @Column(length = 32)
    private String url;

    @Column(length = 32)
    private String driver;

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
