package com.ck.ds.domain;

import javax.sql.DataSource;

public class MysqlDs extends Ds {

    public MysqlDs(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getDsType() {
        return "mysql";
    }

    @Override
    public String getLimitSql(String sql, int pageNum, int pageSize) {
        return sql + " limit " + pageNum * pageSize + "," + pageSize;
    }
}
