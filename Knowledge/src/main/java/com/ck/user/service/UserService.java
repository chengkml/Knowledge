package com.ck.user.service;

import com.ck.common.helper.JdbcQueryHelper;
import com.ck.ds.domain.DsManager;
import com.ck.user.dao.UserRepository;
import com.ck.user.po.UserPo;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DsManager dsManager;

    public UserPo save(UserPo user) {
        userRepo.save(user);
        return user;
    }

    public Page<UserPo> getPage(String keyWord, int pageSize, int pageNum) {
        NamedParameterJdbcTemplate jt = dsManager.getNamedJdbcTemplate();
        StringBuilder listSql = new StringBuilder("select id,name,label,phone,email,create_date,last_upd_date from ck_user where 1=1 ");
        StringBuilder countSql = new StringBuilder("select count(id) from ck_user where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        JdbcQueryHelper.lowerLike("keyWord", keyWord, "and (name like :keyWord or label like :keyWord) ", params, dsManager.getLocalDsType(), listSql, countSql);
        List<UserPo> userPoList = new ArrayList<>();
        jt.queryForList(JdbcQueryHelper.getLimitSql(jt, listSql, pageNum, pageSize), params).forEach(map -> {
            UserPo po = new UserPo();
            po.setId(MapUtils.getLong(map, "id"));
            po.setName(MapUtils.getString(map, "name"));
            po.setLabel(MapUtils.getString(map, "label"));
            po.setPhone(MapUtils.getString(map, "phone"));
            po.setEmail(MapUtils.getString(map, "email"));
            po.setCreateDate((Date) MapUtils.getObject(map, "create_date"));
            po.setLastUpdDate((Date) MapUtils.getObject(map, "last_upd_date"));
            userPoList.add(po);
        });
        return JdbcQueryHelper.toPage(jt, countSql, params, userPoList, pageNum, pageSize);
    }
}
