package com.ck.knowledge.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class EnumVo extends Vo {

    private String value;

    private String label;

    private String descr;
}
