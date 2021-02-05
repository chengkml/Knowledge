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

@Entity(name = "ck_cosmic_demand")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CosmicDemandPo {

    private Long id;

    @Column(length = 256)
    private String demand;

    private Long parentDemandId;

    @Column(name = "demand_user", length = 256)
    private String demandUser;

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
