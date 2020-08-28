package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Get;
import com.ck.knowledge.aop.Post;
import com.ck.knowledge.po.MenuPo;
import com.ck.knowledge.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("菜单接口")
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuServ;

    @Get("tree")
    @ApiOperation("查询菜单树")
    public Object getMenuTree() {
        return menuServ.getMenuTree();
    }

    @Get("list")
    @ApiOperation("查询菜单数据列表")
    public Object getMenuList() {
        return menuServ.getMenuList();
    }

    @Post("save")
    @ApiOperation("保存菜单树")
    public Object saveMenuTree(@RequestBody List<MenuPo> tree) {
        return menuServ.saveMenuTree(tree);
    }
}
