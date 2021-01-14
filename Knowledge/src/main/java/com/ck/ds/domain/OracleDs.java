package com.ck.ds.domain;

import javax.sql.DataSource;

public class OracleDs extends Ds {

    public OracleDs(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getDsType() {
        return "oracle";
    }

    @Override
    public String getLimitSql(String sql, int pageNum, int pageSize) {
        return null;
    }
}
