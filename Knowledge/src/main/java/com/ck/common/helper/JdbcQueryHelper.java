package com.ck.common.helper;

import com.ck.ds.domain.DsManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: SqlHelper
 * @Description: sql拼接工具类
 * @Author: Chengkai
 * @Date: 2019/12/12 16:35
 * @Version: 1.0
 */
public class JdbcQueryHelper {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcQueryHelper.class);

    public static void equals(String name, String param, String sqlSegment, Map<String, Object> params, StringBuilder... sbs) {
        if (StringUtils.isNotBlank(param)) {
            for (StringBuilder sb : sbs) {
                sb.append(sqlSegment);
            }
            params.put(name, param);
        }
    }

    public static void in(String name, List pars, String sqlSegment, Map<String, Object> params, StringBuilder... sbs) {
        if (pars != null && !pars.isEmpty()) {
            for(StringBuilder sb : sbs){
                sb.append(sqlSegment);
            }
            params.put(name, pars);
        }
    }

    public static void lowerLike(String name, String param, String sqlSegment,
                                 Map<String, Object> params, String dsType, StringBuilder... sbs) {
        if (StringUtils.isNotBlank(param)) {
            if ("mysql".equalsIgnoreCase(dsType)) {
                param = param.replaceAll("\\/", "//").replaceAll("\\%", "/%").replaceAll("_", "/_").replaceAll("\\\\", "/\\\\");
                sqlSegment = sqlSegment.replaceAll(name, name + " escape '\\/'");
            } else if ("oracle".equalsIgnoreCase(dsType)) {
                param = param.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\%", "\\\\%").replaceAll("_", "\\\\_");
                sqlSegment = sqlSegment.replaceAll(name, name + " escape chr(92 USING NCHAR_CS)");
            } else {
                LOG.warn("暂不支持对数据库类型为“{}”的模糊查询sql进行特殊字符处理！", dsType);
            }
            for(StringBuilder sb : sbs){
                sb.append(sqlSegment);
            }
            params.put(name, '%' + param.toLowerCase() + '%');
        }
    }



    public static void paramHandledLowerLike(String name, String param, StringBuilder sb, String sqlSegment,
                                             Map<String, Object> params, String dsType) {
        if (StringUtils.isNotBlank(param)) {
            if ("mysql".equalsIgnoreCase(dsType)) {
                sqlSegment = sqlSegment.replaceAll(name, name + " escape '\\/'");
            } else if ("oracle".equalsIgnoreCase(dsType)) {
                sqlSegment = sqlSegment.replaceAll(name, name + " escape chr(92 USING NCHAR_CS)");
            } else {
                LOG.warn("暂不支持对数据库类型为“{}”的模糊查询sql进行特殊字符处理！", dsType);
            }
            sb.append(sqlSegment);
            params.put(name, param.toLowerCase());
        }
    }

    public static void datetimeBetween(String fieldName, String startName, Date startTime, String endName, Date endTime, StringBuilder sb,
                                       Map<String, Object> params, String dsType) {
        if (startTime != null && endTime != null) {
            if (dsType.equals("mysql")) {
                sb.append("and ").append(fieldName).append(" between :").append(startName).append(" and :").append(endName).append(" ");
            } else if (dsType.equals("oracle")) {
                sb.append("and (").append(fieldName).append(" > to_date(:").append(startName).append(",'yyyy-mm-dd hh24:mi:ss') and ").append(fieldName).append("<to_date(:").append(endName).append(",'yyyy-mm-dd hh24:mi:ss')) ");
            } else {
                LOG.warn("暂不支持对数据库类型为“{}”的sql进行日期特殊处理！", dsType);
            }
            params.put(startName, DateHelper.formatDateTime(startTime));
            params.put(endName, DateHelper.formatDateTime(endTime));
        }
    }

    public static String getLimitSql(NamedParameterJdbcTemplate jt, StringBuilder listSql, int pageNum, int pageSize) {
        return BeanHelper.getBean(DsManager.class).getDs().getLimitSql(listSql.toString(), pageNum, pageSize);
    }

    public static Page toPage(NamedParameterJdbcTemplate jt, StringBuilder countSql, Map<String, Object> params, List objListt, int pageNum, int pageSize) {
        return new PageImpl(objListt, PageRequest.of(pageNum, pageSize), jt.queryForObject(countSql.toString(), params, Long.class));
    }

    public static void order(String sortColumn, String sortType, StringBuilder sb) {
        if (StringUtils.isNotBlank(sortColumn)) {
            sb.append(" order by ").append(sortColumn);
            if (StringUtils.isNotBlank(sortType) && sortType.equals("desc")) {
                sb.append(" desc");
            } else {
                sb.append(" asc");
            }
        }
    }

    public static String likePreHandle(String key, String dsType) {
        if ("mysql".equalsIgnoreCase(dsType)) {
            return key.replaceAll("\\/", "//").replaceAll("\\%", "/%").replaceAll("_", "/_").replaceAll("\\\\", "/\\\\");
        } else if ("oracle".equalsIgnoreCase(dsType)) {
            return key.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\%", "\\\\%").replaceAll("_", "\\\\_");
        } else {
            LOG.warn("暂不支持对数据库类型为“{}”的模糊查询sql进行特殊字符处理！", dsType);
            return key;
        }
    }
}
