package com.ck.menu.vo;

import com.ck.common.vo.Vo;
import lombok.Getter;
import lombok.Setter;

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
