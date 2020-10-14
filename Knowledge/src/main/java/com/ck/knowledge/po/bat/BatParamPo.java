package com.ck.knowledge.po.bat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "ck_bat_param")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatParamPo {

    private Long id;

    @Column(length = 32)
    private String name;

    private Long batId;

    private Integer sort;

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }
}
