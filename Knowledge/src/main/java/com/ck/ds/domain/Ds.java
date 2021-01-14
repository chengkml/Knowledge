package com.ck.ds.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Getter
@Setter
public abstract class Ds implements LimitSql {

    public Ds(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        if (namedParameterJdbcTemplate == null) {
            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        }
        return namedParameterJdbcTemplate;
    }

    public abstract String getDsType();

}
