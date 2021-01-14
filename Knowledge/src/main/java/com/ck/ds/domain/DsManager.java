package com.ck.ds.domain;

import com.ck.common.helper.BeanHelper;
import com.ck.common.helper.StringHelper;
import com.ck.ds.dao.DsRepository;
import com.ck.ds.po.DsPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DsManager {

    @Autowired
    private Environment environment;

    @Autowired
    private DsRepository dsRepo;

    private static final String LOCAL_DS = "localDs";

    private ConcurrentHashMap<String, Ds> dsMap = new ConcurrentHashMap<>();

    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return getNamedJdbcTemplate(LOCAL_DS);
    }

    public JdbcTemplate getJdbcTemplate() {
        return getJdbcTemplate(LOCAL_DS);
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate(String dsName) {
        return getDs(dsName).getNamedParameterJdbcTemplate();
    }

    public JdbcTemplate getJdbcTemplate(String dsName) {
        return getDs(dsName).getJdbcTemplate();
    }

    public Ds getDs() {
        if (!dsMap.containsKey(LOCAL_DS)) {
            loadDs(LOCAL_DS);
        }
        return dsMap.get(LOCAL_DS);
    }

    public Ds getDs(String dsName) {
        if (!dsMap.containsKey(dsName)) {
            loadDs(dsName);
        }
        return dsMap.get(dsName);
    }

    private void loadDs(String dsName) {
        if (LOCAL_DS.equals(dsName)) {
            String driverClassName = environment.getProperty("spring.datasource.driverClassName");
            Objects.requireNonNull(driverClassName, "未获取到数据源驱动类配置！");
            if (driverClassName.indexOf("mysql") > 0) {
                dsMap.put(LOCAL_DS, new MysqlDs((DataSource) BeanHelper.getBean("localDs")));
            } else if (driverClassName.indexOf("oracle") > 0) {
                dsMap.put(LOCAL_DS, new OracleDs((DataSource) BeanHelper.getBean("localDs")));
            } else {
                throw new RuntimeException("暂不支持的数据源类型！");
            }
        } else {
            DsPo ds = dsRepo.findByName(dsName);
            Objects.requireNonNull(ds, StringHelper.format("未查询到数据源“{}”的配置信息！", dsName));
            // TODO 载入数据源
        }
    }

    public String getLocalDsType(){
        return getDs().getDsType();
    }

}
