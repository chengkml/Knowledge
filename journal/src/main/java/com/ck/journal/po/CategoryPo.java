package com.ck.journal.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Title: CategoryPo
 * @Author: Chengkai
 * @Date: 2019/8/20 11:01
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "journal_category")
public class CategoryPo {

    private Long id;

    private Integer value;

    @Column(length = 64)
    private String label;

    @Column(length = 8)
    private String type;

    @Column(length = 32)
    private Long parent;

    private Date createDt;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public List<CategoryPo> children;

    @Transient
    public List<CategoryPo> getChildren() {
        return children;
    }
}
