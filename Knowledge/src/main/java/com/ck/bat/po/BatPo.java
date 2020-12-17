package com.ck.bat.po;

import com.ck.res.po.StaticResPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ck_bat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatPo {

    private Long id;

    @Column(length = 32)
    private String name;

    @Column(length = 64)
    private String label;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private String params;

    private StaticResPo bat;

    private Long fileId;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Transient
    public Long getFileId() {
        return fileId;
    }

    @Transient
    public StaticResPo getBat() {
        return bat;
    }
}
