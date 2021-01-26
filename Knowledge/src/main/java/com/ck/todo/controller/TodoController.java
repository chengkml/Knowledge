package com.ck.todo.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.common.properties.CommonProperties;
import com.ck.res.dao.StaticResRepository;
import com.ck.res.po.StaticResPo;
import com.ck.res.service.StaticResService;
import com.ck.todo.po.TodoGroupPo;
import com.ck.todo.po.TodoItemPo;
import com.ck.todo.service.TodoGroupService;
import com.ck.todo.service.TodoItemService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.vfs2.FileObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api("TODO接口")
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoItemService itemServ;

    @Autowired
    private TodoGroupService groupServ;

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    @Autowired
    private CommonProperties commonProperties;

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
                       @RequestParam(value = "states", required = false) List<String> states,
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

    @Post("pushListMail")
    @ApiOperation("推送todo列表")
    public Object generateReport(@RequestParam(name = "size", defaultValue = "-1") int size) throws IOException, TemplateException, MessagingException {
        return itemServ.pushListMail(size);
    }

    @Get("load/res")
    @ApiOperation("载入Todo相关资源")
    public Object loadRes(@RequestParam(name = "todoId") Long todoId) throws IOException, URISyntaxException {
        List<StaticResPo> resPos = resRepo.findByRelaId(todoId);
        List<Long> resIds = new ArrayList<>();
        File dir = new File(commonProperties.getTempDir());
        for (StaticResPo res : resPos) {
            resIds.add(res.getId());
            String suffixName = res.getName().substring(res.getName().lastIndexOf("."));
            File targetFile = new File(dir, res.getMdCode() + suffixName);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            try (FileObject target = resServ.getDirFileObject().resolveFile(String.valueOf(res.getMdCode()));
                 InputStream is = target.getContent().getInputStream();
                 OutputStream os = new FileOutputStream(targetFile)) {
                IOUtils.copy(is, os);
            } catch (Exception e) {
                throw e;
            }
        }
        return resIds;
    }

    @Post("pushItemMail")
    @ApiOperation("推送Todo项目邮件")
    public Object pushItemMail(@RequestBody Long id) throws MessagingException {
        itemServ.pushItemMail(id);
        return id;
    }
}
