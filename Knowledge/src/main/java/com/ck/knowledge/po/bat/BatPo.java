package com.ck.knowledge.po.bat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    List<BatParamPo> params;

    @Transient
    public List<BatParamPo> getParams() {
        return params;
    }

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

}
