package com.ck.ds.domain;

public interface LimitSql {

    String getLimitSql(String sql, int pageNum, int pageSize);

}
