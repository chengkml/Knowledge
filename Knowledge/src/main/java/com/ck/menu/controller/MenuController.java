package com.ck.menu.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.menu.po.MenuPo;
import com.ck.menu.service.MenuService;
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

    @Post("add")
    @ApiOperation("保存菜单")
    public Object saveMenu(@RequestBody MenuPo menu) {
        return menuServ.saveMenu(menu);
    }

    @Post("batch/delete")
    @ApiOperation("批量删除菜单")
    public Object batchDelete(@RequestBody List<Long> menuIds) {
        return menuServ.batchDelete(menuIds);
    }
}
