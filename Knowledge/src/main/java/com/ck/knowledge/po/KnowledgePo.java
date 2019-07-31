package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Title: KnowledgePo
 * @Description: 知识点表
 * @Author: Chengkai
 * @Date: 2019/6/19 21:03
 * @Version: 1.0
 */
@Entity(name="ck_knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePo {

    private Long id;

    private String name;

    private Long category;

    private String descr;

    private Date createDate = new Date();

    private String detail;

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }
}
