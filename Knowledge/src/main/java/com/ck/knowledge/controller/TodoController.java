package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Get;
import com.ck.knowledge.aop.Post;
import com.ck.knowledge.po.todo.TodoGroupPo;
import com.ck.knowledge.po.todo.TodoItemPo;
import com.ck.knowledge.service.TodoGroupService;
import com.ck.knowledge.service.TodoItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("TODO接口")
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoItemService itemServ;

    @Autowired
    private TodoGroupService groupServ;

    @Post("save")
    @ApiOperation("保存Todo项目")
    public Object save(@RequestBody TodoItemPo item) {
        return itemServ.save(item);
    }

    @Post("delete")
    @ApiOperation("删除Todo项目")
    public Object delete(@RequestBody long id) {
        return itemServ.delete(id);
    }

    @Get("list")
    @ApiOperation("查询Todo列表")
    public Object list(@RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize,
                       @RequestParam(value = "states",required = false) List<String> states,
                       @RequestParam(value = "group", defaultValue = "-1") long group,
                       @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
        return itemServ.list(states, keyWord, group, pageNum, pageSize);
    }

    @Post("group/add")
    @ApiOperation("增加Todo分组")
    public Object addGroup(@RequestBody TodoGroupPo group) {
        return groupServ.addGroup(group);
    }

    @Post("group/delete")
    @ApiOperation("删除Todo分组")
    public Object deleteGroup(@RequestBody List<Long> groupIds) {
        return groupServ.deleteGroup(groupIds);
    }

    @Get("group/tree")
    @ApiOperation("查询Todo分组树")
    public Object groupTree() {
        return groupServ.groupTree();
    }

}
