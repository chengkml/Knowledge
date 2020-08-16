package com.ck.ddl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {

    private String name;

    private String dataType;

    private String comment;

    private int length;

    private boolean nullable;

    private boolean isPk;
}
