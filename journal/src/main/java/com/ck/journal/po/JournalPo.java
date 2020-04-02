package com.ck.journal.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Title: JournalPo
 * @Author: Chengkai
 * @Date: 2019/8/16 13:17
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "journal")
public class JournalPo {

    private Long id;

    private String category;

    @Column(length=64)
    private String title;

    @Lob
    private String content;

    private Date createDt;

    @Transient
    private String createDate;

    @Transient
    private String summary;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Lob
    @Column(columnDefinition="TEXT")
    public String getContent(){
        return this.content;
    }
}
