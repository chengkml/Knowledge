package com.ck.knowledge.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

@Setter
@Getter
public class MenuVo extends Vo {

    private Long parentId;

    private String name;

    private String label;

    private String icon;

    private String url;

    private String descr;

    private String valid;

    private List<MenuVo> subMenus;

}
