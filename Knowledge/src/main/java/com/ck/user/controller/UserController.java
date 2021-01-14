package com.ck.user.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.user.dao.UserRepository;
import com.ck.user.po.UserPo;
import com.ck.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("用户接口")
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userServ;

    @Get("page")
    @ApiOperation("查询用户列表")
    public Object getPage(@RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize,
                       @RequestParam(name="keyWord",defaultValue = "") String keyWord) {
        return userServ.getPage(keyWord,pageSize,pageNum);
    }

    @Post("save")
    @ApiOperation("保存用户信息")
    public Object save(@RequestBody UserPo user) {
        return userServ.save(user);
    }

    @Post("delete")
    @ApiOperation("删除用户信息")
    public Object delete(@RequestBody Long userId) {
        userRepo.deleteById(userId);
        return userId;
    }
}
