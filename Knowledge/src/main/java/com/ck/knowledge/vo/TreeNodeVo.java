package com.ck.knowledge.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TreeNodeVo extends EnumVo{

    private List<TreeNodeVo> children;

}
