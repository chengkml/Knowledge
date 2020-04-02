package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Title: CategoryPo
 * @Description: 知识点分类
 * @Author: Chengkai
 * @Date: 2019/6/19 21:06
 * @Version: 1.0
 */
@Entity(name="ck_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPo {

    private Long id;

    @Column(length=32)
    private String name;

    @Column(length=64)
    private String label;

    @Column(name="parent_id")
    private Long parentId;

    @Column(name="create_date")
    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    public List<CategoryPo> children;

    @Transient
    public List<CategoryPo> getChildren() {
        return children;
    }

    public CategoryPo(Long id, String name, String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }
}
