package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ck_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuPo {

    private Long id;

    private Long parentId;

    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String label;

    @Column(length = 32)
    private String icon;

    @Column(length = 128)
    private String url;

    @Column(length = 512)
    private String descr;

    private Integer sort;

    @Column(length = 8)
    private String valid;

    private List<MenuPo> subMenus;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Transient
    public List<MenuPo> getSubMenus() {
        return subMenus;
    }
}
