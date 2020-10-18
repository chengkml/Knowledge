package com.ck.knowledge.po.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "ck_res")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticResPo {

    private Long id;

    @Column(length = 32)
    private String name;

    @Column(name = "rela_id")
    private Long relaId;

    @Column(name = "res_url", length = 128)
    private String resUrl;

    private Long size;

    private String path;
    
    @Column(length = 8)
    private String valid;
    

    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

}
