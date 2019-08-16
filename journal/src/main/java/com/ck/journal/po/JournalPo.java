package com.ck.journal.po;

import javax.persistence.*;
import java.util.Date;

/**
 * @Title: JournalPo
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2019/8/16 13:17
 * @Version: 1.0
 */
@Entity
@Table(name="journal")
public class JournalPo {
    @Id
    @Column(name="journal_id",length=32)
    private String id;

    @Lob
    private String content;

    private Date createDt;
}
